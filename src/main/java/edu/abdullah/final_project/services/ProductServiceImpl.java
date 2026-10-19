package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.ProductFormDTO;
import edu.abdullah.final_project.entities.Manufacturer;
import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;
import edu.abdullah.final_project.exceptions.ProductNotFoundException;
import edu.abdullah.final_project.repositories.CartItemRepository;
import edu.abdullah.final_project.repositories.ManufacturerRepository;
import edu.abdullah.final_project.repositories.OrderRepository;
import edu.abdullah.final_project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Value("${app.images.path}")
    private String imagesPath;

    public ProductServiceImpl(ProductRepository productRepository,
                              ManufacturerRepository manufacturerRepository,
                              CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @Override
    public Optional<Product> getActiveProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .filter(Product::isActive);
    }

    @Override
    public List<Product> searchProducts(String keyword, Category category,
                                        Difficulty difficulty,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> results = isAdmin()
                ? productRepository.findAll()
                : productRepository.findByActiveTrue();

        if (keyword != null && !keyword.isBlank()) {
            String lower = keyword.toLowerCase();
            results = results.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lower)
                            || p.getShortDescription().toLowerCase().contains(lower))
                    .toList();
        }

        if (category != null) {
            results = results.stream()
                    .filter(p -> p.getCategory() == category)
                    .toList();
        }

        if (difficulty != null) {
            results = results.stream()
                    .filter(p -> p.getDifficulty() == difficulty)
                    .toList();
        }

        if (minPrice != null) {
            results = results.stream()
                    .filter(p -> p.getPrice().compareTo(minPrice) >= 0)
                    .toList();
        }

        if (maxPrice != null) {
            results = results.stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .toList();
        }

        return results;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    @Override
    public void createProduct(ProductFormDTO dto) {
        Manufacturer manufacturer = manufacturerRepository.findByName(dto.getManufacturerName())
                .orElseThrow(() -> new IllegalArgumentException("Manufacturer not found: "
                        + dto.getManufacturerName()));

        Product product = new Product();
        mapDtoToProduct(dto, product, manufacturer);
        product.setAcquiredDate(LocalDate.now());
        product.setActive(true);

        productRepository.save(product);
    }

    @Override
    public void updateProduct(String productCode, ProductFormDTO dto) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productCode));

        Manufacturer manufacturer = manufacturerRepository.findByName(dto.getManufacturerName())
                .orElseThrow(() -> new IllegalArgumentException("Manufacturer not found: "
                        + dto.getManufacturerName()));

        mapDtoToProduct(dto, product, manufacturer);
        productRepository.save(product);
    }

    @Override
    public void activateProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productCode));
        product.setActive(true);
        productRepository.save(product);
    }

    @Override
    public void deactivateProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productCode));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        if (cartItemRepository.existsByProduct(product)
                || orderRepository.existsByItemsProduct(product)) {
            product.setActive(false);
            productRepository.save(product);
            throw new IllegalArgumentException(
                    "Product is associated with existing carts or orders and cannot be deleted. It has been deactivated instead.");
        }

        // Delete associated images
        deleteProductImages(productCode);

        productRepository.delete(product);
    }

    private void deleteProductImages(String productCode) {
        File folder = new File(imagesPath);
        File[] images = folder.listFiles(
                (dir, name) -> name.startsWith(productCode + "-") && name.endsWith(".jpg")
        );

        if (images != null) {
            for (File image : images) {
                image.delete();
            }
        }
    }

    private void mapDtoToProduct(ProductFormDTO dto, Product product, Manufacturer manufacturer) {
        product.setProductCode(dto.getProductCode());
        product.setName(dto.getName());
        product.setManufacturer(manufacturer);
        product.setPieceCount(dto.getPieceCount());
        product.setDifficulty(dto.getDifficulty());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setWholesaleCost(dto.getWholesaleCost());
        product.setShortDescription(dto.getShortDescription());
        product.setLongDescription(dto.getLongDescription());
    }
    @Override
    public Optional<Product> getProductForUser(String productCode) {
        return isAdmin()
                ? getProductByCode(productCode)
                : getActiveProductByCode(productCode);
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

}



