package services;

import models.Category;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CategoryRepository;
import repositories.ProductRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category addCategory(Category category, Long parentId) {
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, Category updatedCategory, Long parentId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (updatedCategory.getNom() != null)
            category.setNom(updatedCategory.getNom());

        if (updatedCategory.getDescription() != null)
            category.setDescription(updatedCategory.getDescription());

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        } else
            category.setParent(null);

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId))
            throw new RuntimeException("Category not found");

        categoryRepository.deleteById(categoryId);
    }

    public void addSubCategory(Long parentId, Long subCategoryId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));
        Category subCategory = categoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("Sub-category not found"));

        subCategory.setParent(parent);
        categoryRepository.save(subCategory);
    }

    public void removeSubCategory(Long subCategoryId) {
        Category subCategory = categoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("Sub-category not found"));

        subCategory.setParent(null);
        categoryRepository.save(subCategory);
    }

    public void addProductToCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setCategory(category);
        productRepository.save(product);
    }

    public void removeProductFromCategory(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setCategory(null);
        productRepository.save(product);
    }

}
