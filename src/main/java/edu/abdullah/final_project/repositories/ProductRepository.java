package edu.abdullah.final_project.repositories;

import edu.abdullah.final_project.entities.Manufacturer;
import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    List<Product> findByActiveTrue();

    List<Product> findByActiveFalse();

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryAndActiveTrue(Category category);

    List<Product> findByDifficultyAndActiveTrue(Difficulty difficulty);

    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByManufacturer(Manufacturer manufacturer);

    boolean existsByManufacturerId(Long manufacturerId);
}