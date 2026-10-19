package edu.abdullah.final_project.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productNameAtPurchase;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceAtPurchase;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    public OrderItem() {}

    public OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.productNameAtPurchase = product.getName();
        this.unitPriceAtPurchase = product.getPrice();
        this.quantity = quantity;
        this.lineTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getProductNameAtPurchase() { return productNameAtPurchase; }
    public void setProductNameAtPurchase(String productNameAtPurchase) { this.productNameAtPurchase = productNameAtPurchase; }

    public BigDecimal getUnitPriceAtPurchase() { return unitPriceAtPurchase; }
    public void setUnitPriceAtPurchase(BigDecimal unitPriceAtPurchase) { this.unitPriceAtPurchase = unitPriceAtPurchase; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}