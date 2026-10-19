package edu.abdullah.final_project.controllers;

import edu.abdullah.final_project.dtos.ChangePasswordDTO;
import edu.abdullah.final_project.dtos.EditAccountDTO;
import edu.abdullah.final_project.entities.User;
import edu.abdullah.final_project.services.AuthService;
import edu.abdullah.final_project.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String account(Model model, HttpSession session) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("recentOrders", userService.getOrderHistory()
                .stream().limit(3).toList());
        model.addAttribute("errorMessage", authService.consumeError(session));
        model.addAttribute("successMessage", authService.consumeSuccess(session));
        return "account";
    }

    @GetMapping("/edit")
    public String showEditAccount(Model model, HttpSession session) {
        User user = userService.getCurrentUser();
        EditAccountDTO dto = new EditAccountDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        model.addAttribute("user", user);
        model.addAttribute("editForm", dto);
        model.addAttribute("errorMessage", authService.consumeError(session));
        model.addAttribute("successMessage", authService.consumeSuccess(session));
        return "edit-account";
    }

    @PostMapping("/edit")
    public String editAccount(@ModelAttribute("editForm") EditAccountDTO dto,
                              HttpSession session) {
        userService.updateAccount(dto);
        authService.flashSuccess(session, "Account updated successfully.");
        return "redirect:/account";
    }

    @GetMapping("/change-password")
    public String showChangePassword(Model model, HttpSession session) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("passwordForm", new ChangePasswordDTO());
        model.addAttribute("errorMessage", authService.consumeError(session));
        model.addAttribute("successMessage", authService.consumeSuccess(session));
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("passwordForm") ChangePasswordDTO dto,
                                 HttpSession session) {
        userService.changePassword(dto);
        authService.flashSuccess(session, "Password changed successfully.");
        return "redirect:/account";
    }

    @GetMapping("/orders")
    public String orderHistory(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("orders", userService.getOrderHistory());
        return "order-history";
    }

    @GetMapping("/orders/{orderNumber}")
    public String orderDetails(@PathVariable String orderNumber, Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("order", userService.getOrderByNumber(orderNumber));
        return "order-details";
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