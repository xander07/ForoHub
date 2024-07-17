package com.aluracursos.forohub.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Configures the security filter chain for HTTP requests.
     * @param httpSecurity HttpSecurity object to configure security settings.
     * @return SecurityFilterChain configured with specified settings.
     * @throws Exception If configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
         httpSecurity.csrf(csrf -> csrf.disable());// Disable CSRF protection
        httpSecurity.sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy
                        .STATELESS))// Use stateless session management
                .authorizeHttpRequests((authorizeRequests) ->
                 authorizeRequests
                 // Permit access to /login endpoint
                .requestMatchers(HttpMethod.POST, "/login")
                .permitAll()
                 // Permit access to /register endpoint
                .requestMatchers(HttpMethod.POST, "/register")
                .permitAll()
                .requestMatchers("/swagger-ui.html", "/v3/api-docs/**","/swagger-ui/**")
                .permitAll()
                .anyRequest()
                .authenticated())// Authenticate all other requests
                // Add custom security filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        ;

        // Build and return the configured SecurityFilterChain
        return httpSecurity.build();
    }

    /**
     * Provides the AuthenticationManager bean required for authentication.
     * @param authenticationConfiguration Configuration for authentication.
     * @return AuthenticationManager bean.
     * @throws Exception If authentication manager cannot be retrieved.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**
     * Provides the BCryptPasswordEncoder bean for encoding passwords.
     * @return BCryptPasswordEncoder bean.
     */
    @Bean
    public BCryptPasswordEncoder PasswordEncoder(){
        // Return BCryptPasswordEncoder bean
        return new BCryptPasswordEncoder();
    }
}
