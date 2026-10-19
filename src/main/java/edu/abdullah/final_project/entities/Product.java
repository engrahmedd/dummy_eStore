package edu.abdullah.final_project.entities;

import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(nullable = false)
    private Integer pieceCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal wholesaleCost;

    @Column(nullable = false, length = 500)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String longDescription;

    @Column(nullable = false)
    private LocalDate acquiredDate;

    @Column(nullable = false)
    private boolean active = true;

    public Product() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Manufacturer getManufacturer() { return manufacturer; }
    public void setManufacturer(Manufacturer manufacturer) { this.manufacturer = manufacturer; }
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
    public LocalDate getAcquiredDate() { return acquiredDate; }
    public void setAcquiredDate(LocalDate acquiredDate) { this.acquiredDate = acquiredDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}