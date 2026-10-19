package edu.abdullah.final_project.dtos;

import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductFormDTO {

    @NotBlank(message = "Product code is required.")
    private String productCode;

    @NotBlank(message = "Product name is required.")
    private String name;

    @NotBlank(message = "Manufacturer is required.")
    private String manufacturerName;

    @NotNull(message = "Piece count is required.")
    @Min(value = 1, message = "Piece count must be at least 1.")
    private Integer pieceCount;

    @NotNull(message = "Difficulty is required.")
    private Difficulty difficulty;

    @NotNull(message = "Category is required.")
    private Category category;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0.")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount.")
    private BigDecimal price;

    @NotNull(message = "Wholesale cost is required.")
    @DecimalMin(value = "0.01", message = "Wholesale cost must be greater than 0.")
    @Digits(integer = 8, fraction = 2, message = "Wholesale cost must be a valid monetary amount.")
    private BigDecimal wholesaleCost;

    @NotBlank(message = "Short description is required.")
    @Size(max = 500, message = "Short description cannot exceed 500 characters.")
    private String shortDescription;

    @NotBlank(message = "Long description is required.")
    private String longDescription;

    public static ProductFormDTO fromProduct(Product product) {
        ProductFormDTO dto = new ProductFormDTO();
        dto.setProductCode(product.getProductCode());
        dto.setName(product.getName());
        dto.setManufacturerName(product.getManufacturer().getName());
        dto.setPieceCount(product.getPieceCount());
        dto.setDifficulty(product.getDifficulty());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setWholesaleCost(product.getWholesaleCost());
        dto.setShortDescription(product.getShortDescription());
        dto.setLongDescription(product.getLongDescription());
        return dto;
    }


    // Getters and setters
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturerName() { return manufacturerName; }
    public void setManufacturerName(String manufacturerName) { this.manufacturerName = manufacturerName; }

    public Integer getPieceCount() { return pieceCount; }
    public void setPieceCount(Integer pieceCount) { this.pieceCount = pieceCount; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getWholesaleCost() { return wholesaleCost; }
    public void setWholesaleCost(BigDecimal wholesaleCost) { this.wholesaleCost = wholesaleCost; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }
}