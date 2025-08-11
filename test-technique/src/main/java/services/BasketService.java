package services;

import models.Basket;
import models.BasketItem;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BasketItemRepository;
import repositories.BasketRepository;
import repositories.ProductRepository;

import java.util.Optional;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public BasketService(BasketRepository basketRepository, BasketItemRepository basketItemRepository,
                         ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.basketItemRepository = basketItemRepository;
        this.productRepository = productRepository;
    }

    public Basket createBasket() {
        return basketRepository.save(new Basket());
    }

    public Basket addProductToBasket(Long basketId, Long productId, int quantity) {

        Basket basket = basketRepository.findById(basketId).orElseThrow(() -> new RuntimeException("Basket not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        int existingQuantity = basket.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .map(BasketItem::getQuantity)
                .findFirst()
                .orElse(0);

        int newQuantity = existingQuantity + quantity;

        if (newQuantity > product.getQuantiteStock())
            throw new RuntimeException("Quantity requested exceeds available stock");

        Optional<BasketItem> existingItem = basket.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            BasketItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            basketItemRepository.save(item);
        } else {
            BasketItem newItem = new BasketItem();
            newItem.setBasket(basket);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            basket.getItems().add(newItem);
            basketItemRepository.save(newItem);
        }

        return basketRepository.save(basket);
    }

    public Basket updateProductQuantity(Long basketId, Long productId, int quantity) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(() -> new RuntimeException("Basket not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (quantity > product.getQuantiteStock())
            throw new RuntimeException("Quantity requested exceeds available stock");

        BasketItem item = basket.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in basket"));

        if (quantity <= 0) {
            basket.getItems().remove(item);
            basketItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            basketItemRepository.save(item);
        }

        return basketRepository.save(basket);
    }

    public Basket removeProductFromBasket(Long basketId, Long productId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(() -> new RuntimeException("Basket not found"));

        BasketItem item = basket.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in basket"));

        basket.getItems().remove(item);
        basketItemRepository.delete(item);

        return basketRepository.save(basket);
    }

    public double getTotal(Long basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(() -> new RuntimeException("Basket not found"));
        return basket.getTotal();
    }

    public Optional<Basket> getBasket(Long basketId) {
        return basketRepository.findById(basketId);
    }
}
