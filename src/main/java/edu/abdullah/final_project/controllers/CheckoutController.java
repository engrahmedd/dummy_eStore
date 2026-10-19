package edu.abdullah.final_project.controllers;

import edu.abdullah.final_project.dtos.CheckoutDTO;
import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.entities.Order;
import edu.abdullah.final_project.services.AuthService;
import edu.abdullah.final_project.services.CartService;
import edu.abdullah.final_project.services.CheckoutService;
import edu.abdullah.final_project.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CartService cartService;
    private final AuthService authService;
    private final UserService userService;

    public CheckoutController(CheckoutService checkoutService,
                              CartService cartService,
                              AuthService authService, UserService userService) {
        this.checkoutService = checkoutService;
        this.cartService = cartService;
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping
    public String showCheckout(Model model, HttpSession session) {
        Cart cart = cartService.getCurrentUserCart();

        if (cart.getItems().isEmpty()) {
            authService.flashError(session, "Your cart is empty.");
            return "redirect:/cart";
        }

        model.addAttribute("checkoutForm", new CheckoutDTO());
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.getCartTotal(cart));
        model.addAttribute("errorMessage", authService.consumeError(session));
        return "checkout";
    }

    @PostMapping
    public String processCheckout(@ModelAttribute("checkoutForm") CheckoutDTO dto,
                                  HttpSession session) {
        Order order = checkoutService.checkout(dto);
        session.setAttribute("lastOrder", order);
        return "redirect:/checkout/success";
    }

    @GetMapping("/success")
    public String checkoutSuccess(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("lastOrder");
        if (order == null) {
            return "redirect:/products";
        }
        session.removeAttribute("lastOrder");
        model.addAttribute("order", order);
        return "checkout-success";
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
}