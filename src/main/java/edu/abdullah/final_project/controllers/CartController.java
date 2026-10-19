package edu.abdullah.final_project.controllers;

import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.services.AuthService;
import edu.abdullah.final_project.services.CartService;
import edu.abdullah.final_project.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final AuthService authService;
    private final UserService userService;

    public CartController(CartService cartService, AuthService authService, UserService userService) {
        this.cartService = cartService;
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping
    public String viewCart(Model model) {
        Cart cart = cartService.getCurrentUserCart();
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.getCartTotal(cart));
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String productCode,
                            @RequestParam int quantity,
                            HttpSession session) {
        cartService.addToCart(productCode, quantity);
        authService.flashSuccess(session, "Item added to cart.");
        return "redirect:/products/" + productCode;
    }

    @GetMapping("/add-redirect")
    public String addToCartRedirect(@RequestParam String productCode,
                                    @RequestParam int quantity,
                                    HttpSession session) {
        session.setAttribute("pendingProductCode", productCode);
        session.setAttribute("pendingQuantity", quantity);
        return "redirect:/login";
    }

    @GetMapping("/add-after-login")
    public String addAfterLogin(@RequestParam String productCode,
                                @RequestParam int quantity,
                                HttpSession session) {
        cartService.addToCart(productCode, quantity);
        authService.flashSuccess(session, "Item added to cart.");
        return "redirect:/products/" + productCode;
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                 @RequestParam int quantity) {
        cartService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "redirect:/cart";
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

    @GetMapping("/pending")
    public String pendingAdd(@RequestParam String productCode,
                             @RequestParam int quantity,
                             HttpSession session) {
        cartService.addToCart(productCode, quantity);
        authService.flashSuccess(session, "Item added to cart.");
        return "redirect:/products/" + productCode;
    }
}