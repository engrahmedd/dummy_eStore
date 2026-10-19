package edu.abdullah.final_project.controllers;

import edu.abdullah.final_project.dtos.ProductFormDTO;
import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;
import edu.abdullah.final_project.exceptions.ProductNotFoundException;
import edu.abdullah.final_project.services.AuthService;
import edu.abdullah.final_project.services.ProductService;
import edu.abdullah.final_project.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final AuthService authService;

    public ProductController(ProductService productService, UserService userService, AuthService authService) {
        this.productService = productService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.getAllActiveProducts()
                .stream().limit(4).toList());
        model.addAttribute("categories", Category.values());
        model.addAttribute("totalProducts", productService.getAllActiveProducts().size());
        return "home";
    }

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model, HttpSession session) {

        model.addAttribute("products", productService.searchProducts(keyword, category, difficulty, minPrice, maxPrice));
        model.addAttribute("categories", Category.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("errorMessage", authService.consumeError(session));
        model.addAttribute("successMessage", authService.consumeSuccess(session));
        return "products";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/products/{code}")
    public String productDetails(@PathVariable String code, Model model) {
        return productService.getProductForUser(code)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-details";
                })
                .orElse("redirect:/products");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products/create")
    public String showCreateProduct(Model model) {
        model.addAttribute("productForm", new ProductFormDTO());
        model.addAttribute("manufacturers", productService.getAllManufacturers());
        model.addAttribute("categories", Category.values());
        model.addAttribute("difficulties", Difficulty.values());
        return "product-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/create")
    public String createProduct(@Valid @ModelAttribute("productForm") ProductFormDTO dto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("manufacturers", productService.getAllManufacturers());
            model.addAttribute("categories", Category.values());
            model.addAttribute("difficulties", Difficulty.values());
            return "product-form";
        }
        productService.createProduct(dto);
        return "redirect:/products/" + dto.getProductCode();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products/edit/{code}")
    public String showEditProduct(@PathVariable String code, Model model) {
        return productService.getProductByCode(code)
                .map(product -> {
                    model.addAttribute("productForm", ProductFormDTO.fromProduct(product));
                    model.addAttribute("manufacturers", productService.getAllManufacturers());
                    model.addAttribute("categories", Category.values());
                    model.addAttribute("difficulties", Difficulty.values());
                    model.addAttribute("isEdit", true);
                    return "product-form";
                })
                .orElse("redirect:/products");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/edit/{code}")
    public String editProduct(@PathVariable String code,
                              @Valid @ModelAttribute("productForm") ProductFormDTO dto,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("manufacturers", productService.getAllManufacturers());
            model.addAttribute("categories", Category.values());
            model.addAttribute("difficulties", Difficulty.values());
            model.addAttribute("isEdit", true);
            return "product-form";
        }
        productService.updateProduct(code, dto);
        return "redirect:/products/" + code;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/activate/{code}")
    public String activateProduct(@PathVariable String code) {
        productService.activateProduct(code);
        return "redirect:/products/" + code;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/deactivate/{code}")
    public String deactivateProduct(@PathVariable String code) {
        productService.deactivateProduct(code);
        return "redirect:/products/" + code;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/delete/{code}")
    public String deleteProduct(@PathVariable String code, HttpSession session) {
        productService.deleteProduct(code);
        return "redirect:/products";
    }

    @ModelAttribute("welcomeName")
    public String welcomeName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth.getName().equals("anonymousUser")) {
            return null;
        }
        return userService.getCurrentUser().getFirstName();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/products";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(ProductNotFoundException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/products";
    }
}