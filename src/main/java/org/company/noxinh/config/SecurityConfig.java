package org.company.noxinh.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    // List API
    private static final String AUTHENTICATION_API = "/api/v1/auth/**";
    private static final String ACCOUNT_API = "/api/v1/account/**";
    private static final String ADDRESS_API = "/api/v1/address/**";
    private static final String ADMIN_API = "/api/v1/admin/**";
    private static final String CATEGORY_API = "/api/v1/category/**";
    private static final String BANNER_API = "/api/v1/banner/**";
    private static final String PRODUCT_API = "/api/v1/product/**";
    private static final String COMBO_API = "/api/v1/combo/**";
    private static final String SLIDER_API = "/api/v1/slider/**";
    private static final String UPLOAD_FILE_API = "/api/v1/file/**";
    private static final String TEST_API = "/api/v1/test/**";

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(ACCOUNT_API).authenticated()
//                                .requestMatchers(ADMIN_API).hasAuthority("ADMIN")
                                .requestMatchers(AUTHENTICATION_API,
                                        ADDRESS_API,
                                        CATEGORY_API,
                                        BANNER_API,
                                        PRODUCT_API,
                                        SLIDER_API,
                                        COMBO_API,
                                        TEST_API,
                                        ADMIN_API,
                                        UPLOAD_FILE_API,
                                        "/api/v1/combo/",
                                        "/api/v1/order/",
                                        "/api/v1/order/download-excel",
                                        "/api/v1/order/list-clients",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/api/v1/file/**",
                                        "/ws/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .logout(
                        log -> log.logoutUrl(AUTHENTICATION_API + "/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    /**
     * Cors configuration source cors configuration source.
     *
     * @return the cors configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5174",
                "http://localhost:5173",
                "https://localhost:5174",
                "https://localhost:5173",
                "https://resilient-salmiakki-e634e0.netlify.app",
                "https://noxinh-admin.netlify.app",
                "https://symphonious-kheer-4c84b0.netlify.app/",
                "https://noxinh.net",
                "https://noxinh.com.vn",
                "https://noxinh.com",
                "https://noxinh.vn"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Cors filter.
     *
     * @return the cors filter
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
