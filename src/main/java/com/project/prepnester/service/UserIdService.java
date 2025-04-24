package com.project.prepnester.service;

import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserIdService {

  private final UserRepository userRepository;

  public UUID getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalArgumentException("User not authenticated");
    }

    String email = ((User) authentication.getPrincipal()).getUsername();

    PrepNesterUserDetails user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException("User with email " + email + " not found"));

    return user.getId();
  }
}
