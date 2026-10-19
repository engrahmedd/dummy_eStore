package edu.abdullah.final_project.controllers;


import edu.abdullah.final_project.dtos.RegistrationDTO;
import edu.abdullah.final_project.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        model.addAttribute("errorMessage", authService.consumeError(session));
        model.addAttribute("successMessage", authService.consumeSuccess(session));
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(RegistrationDTO dto, HttpSession session) {
        authService.register(dto, session);
        return "redirect:/login";
    }

}
