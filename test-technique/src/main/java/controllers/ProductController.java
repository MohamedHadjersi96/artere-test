package controllers;

import dtos.ProductDto;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto,
                                              @RequestParam Long categoryId) {

        Product product = new Product();
        product.setNom(productDto.getNom());
        product.setPrix(productDto.getPrix());
        product.setQuantiteStock(productDto.getQuantiteStock());

        Product created = productService.addProduct(product, productDto.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody ProductDto productDTO) {
        Product product = new Product();
        product.setId(id);
        product.setNom(productDTO.getNom());
        product.setPrix(productDTO.getPrix());
        product.setQuantiteStock(productDTO.getQuantiteStock());
        Product updated = productService.updateProduct(product);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
