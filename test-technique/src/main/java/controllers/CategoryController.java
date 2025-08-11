package controllers;

import dtos.CategoryDto;
import models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CategoryService;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDto categoryDto) {
        Category category = new Category();
        category.setNom(categoryDto.getNom());
        category.setDescription(categoryDto.getDescription());

        Category created = categoryService.addCategory(category, categoryDto.getParentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id,
                                                   @RequestBody CategoryDto categoryDto) {
        Category updatedCategory = new Category();
        updatedCategory.setNom(categoryDto.getNom());
        updatedCategory.setDescription(categoryDto.getDescription());

        Category updated = categoryService.updateCategory(id, updatedCategory, categoryDto.getParentId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{parentId}/subcategories/{subCategoryId}")
    public ResponseEntity<Void> addSubCategory(@PathVariable Long parentId,
                                               @PathVariable Long subCategoryId) {
        categoryService.addSubCategory(parentId, subCategoryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/subcategories/{subCategoryId}")
    public ResponseEntity<Void> removeSubCategory(@PathVariable Long subCategoryId) {
        categoryService.removeSubCategory(subCategoryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> addProductToCategory(@PathVariable Long categoryId,
                                                     @PathVariable Long productId) {
        categoryService.addProductToCategory(categoryId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> removeProductFromCategory(@PathVariable Long productId) {
        categoryService.removeProductFromCategory(productId);
        return ResponseEntity.ok().build();
    }

}
