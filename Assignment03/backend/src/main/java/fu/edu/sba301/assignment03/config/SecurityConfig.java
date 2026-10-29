package fu.edu.sba301.assignment03.config;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(org.springframework.security.config.Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Requirement: "Do not need authentication to view the room information in this system"
                .requestMatchers(HttpMethod.GET, "/api/rooms/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/room-types/**").permitAll()

                // Staff-only endpoints
                .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasRole("STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("STAFF")
                .requestMatchers(HttpMethod.GET, "/api/customers").hasRole("STAFF")
                .requestMatchers(HttpMethod.GET, "/api/customers/{id:[0-9]+}").hasRole("STAFF")
                .requestMatchers(HttpMethod.POST, "/api/customers").hasRole("STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole("STAFF")
                .requestMatchers(HttpMethod.GET, "/api/bookings").hasRole("STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/bookings/{id}/status").hasRole("STAFF")

                // Authenticated (Customer or Staff)
                .requestMatchers("/api/customers/profile").authenticated()
                .requestMatchers("/api/bookings/my-history").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/bookings").authenticated()

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                })
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
