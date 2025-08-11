package controllers;

import models.Basket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.BasketService;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping
    public Basket createBasket() {
        return basketService.createBasket();
    }

    @PostMapping("/{basketId}/add")
    public Basket addProductToBasket(@PathVariable Long basketId,
                                     @RequestParam Long productId,
                                     @RequestParam int quantity) {
        return basketService.addProductToBasket(basketId, productId, quantity);
    }

    @PutMapping("/{basketId}/update")
    public Basket updateProductQuantity(@PathVariable Long basketId,
                                        @RequestParam Long productId,
                                        @RequestParam int quantity) {
        return basketService.updateProductQuantity(basketId, productId, quantity);
    }

    @DeleteMapping("/{basketId}/remove")
    public Basket removeProductFromBasket(@PathVariable Long basketId,
                                          @RequestParam Long productId) {
        return basketService.removeProductFromBasket(basketId, productId);
    }

    @GetMapping("/{basketId}/total")
    public double getTotal(@PathVariable Long basketId) {
        return basketService.getTotal(basketId);
    }

    @GetMapping("/{basketId}")
    public Basket getBasket(@PathVariable Long basketId) {
        return basketService.getBasket(basketId).orElseThrow();
    }
}
