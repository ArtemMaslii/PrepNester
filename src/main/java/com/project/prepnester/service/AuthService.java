package com.project.prepnester.service;

import com.project.prepnester.dto.request.LoginDto;
import com.project.prepnester.util.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  public String login(LoginDto loginDto) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return jwtTokenProvider.generateToken(authentication);
  }
}