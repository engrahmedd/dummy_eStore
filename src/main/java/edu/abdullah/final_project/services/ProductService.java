package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.ProductFormDTO;
import edu.abdullah.final_project.entities.Manufacturer;
import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllActiveProducts();
    Optional<Product> getActiveProductByCode(String productCode);
    List<Product> searchProducts(String keyword, Category category, Difficulty difficulty,
                                 BigDecimal minPrice, BigDecimal maxPrice);
    List<Manufacturer> getAllManufacturers();

    List<Product> getAllProducts();
    Optional<Product> getProductByCode(String productCode);
    void createProduct(ProductFormDTO dto);
    void updateProduct(String productCode, ProductFormDTO dto);
    void activateProduct(String productCode);
    void deactivateProduct(String productCode);
    void deleteProduct(String productCode);
    Optional<Product> getProductForUser(String productCode);

}