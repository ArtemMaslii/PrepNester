package com.project.prepnester.controller;

import com.project.prepnester.dto.request.LoginDto;
import com.project.prepnester.dto.response.JwtAuthResponse;
import com.project.prepnester.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
    String token = authService.login(loginDto);

    JwtAuthResponse jwtAuthResponse = JwtAuthResponse.builder()
        .accessToken(token)
        .tokenType("Bearer")
        .build();

    return ResponseEntity.ok(jwtAuthResponse);
  }
}
