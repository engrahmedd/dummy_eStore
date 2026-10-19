package edu.abdullah.final_project.exceptions;

import edu.abdullah.final_project.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final AuthService authService;

    public GlobalExceptionHandler(AuthService authService) {
        this.authService = authService;
    }

    @ExceptionHandler(PaymentDeclinedException.class)
    public String handlePaymentDeclined(PaymentDeclinedException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/checkout";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/checkout";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(ProductNotFoundException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/products";
    }

    @ExceptionHandler(CartOwnershipException.class)
    public String handleCartOwnership(CartOwnershipException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/cart";
    }

    @ExceptionHandler(EmptyCartException.class)
    public String handleEmptyCart(EmptyCartException e, HttpSession session) {
        authService.flashError(session, e.getMessage());
        return "redirect:/cart";
    }
}