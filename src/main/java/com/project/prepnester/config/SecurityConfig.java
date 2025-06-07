package com.project.prepnester.config;

import com.project.prepnester.util.jwt.JwtAuthenticationEntryPoint;
import com.project.prepnester.util.jwt.JwtAuthenticationFilter;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  @Value("${spring.graphql.cors.allowed-origins}")
  private String[] allowedOrigins;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions().disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .successHandler(oAuth2AuthenticationSuccessHandler)
        )
        .httpBasic(Customizer.withDefaults())
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
