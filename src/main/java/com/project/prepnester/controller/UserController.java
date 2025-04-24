package com.project.prepnester.controller;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.service.UserDetailsService;
import com.project.prepnester.validation.ValidEmail;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/user")
@Validated
public class UserController {

  private final UserDetailsService userDetailsService;

  @GetMapping
  public ResponseEntity<UserDetailsResponse> getUserDetails(
      @RequestParam @ValidEmail String email) {
    return ResponseEntity.ok(userDetailsService.getUserDetails(email));
  }

  @PostMapping("/register")
  public ResponseEntity<UserDetailsResponse> registerUser(
      @RequestBody @Valid UserDetailsRequest userDetails) {
    return ResponseEntity.ok(userDetailsService.registerUser(userDetails));
  }
}
