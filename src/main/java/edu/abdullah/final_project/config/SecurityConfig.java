package edu.abdullah.final_project.config;

import edu.abdullah.final_project.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .requestCache(cache -> cache
                        .requestCache(requestCache())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/home", "/products", "/products/**",
                                "/login", "/register", "/error","/favicon.ico",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/cart/**", "/checkout/**",
                                "/account/**", "/orders/**"
                        ).hasAnyRole("CUSTOMER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(authSuccessHandler())
                        .failureHandler(authFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/products");
        handler.setAlwaysUseDefaultTargetUrl(false);
        return handler;
    }

    @Bean
    public AuthenticationFailureHandler authFailureHandler() {
        return (request, response, exception) -> {
            authService.flashError(request.getSession(),
                    "Invalid email or password. Please try again.");
            response.sendRedirect("/login");
        };
    }

    @Bean
    public HttpSessionRequestCache requestCache() {
        HttpSessionRequestCache cache = new HttpSessionRequestCache();
        cache.setMatchingRequestParameterName(null);
        return cache;
    }
}