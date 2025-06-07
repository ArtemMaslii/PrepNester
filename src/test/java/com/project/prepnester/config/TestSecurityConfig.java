//package com.project.prepnester.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@Profile("test")
//@EnableWebSecurity
//public class TestSecurityConfig {
//
//  @Bean
//  public UserDetailsService userDetailsService() {
//    UserDetails user = User.withUsername("alice@example.com")
//        .password("{noop}password_hash1") // or use encoded password
//        .roles("USER")
//        .build();
//    return new InMemoryUserDetailsManager(user);
//  }
//
//  @Bean
//  public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
//        .httpBasic(Customizer.withDefaults());
//    return http.build();
//  }
//}
