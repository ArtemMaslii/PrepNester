package com.project.prepnester.service;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.model.userDetails.Role;
import com.project.prepnester.repository.RoleRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional
public class UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public UserDetailsResponse getUserDetails(String email) {
    PrepNesterUserDetails user = userRepository.findByEmail(email).orElseThrow(
        () -> new NotFoundException("User with email " + email + " not found")
    );

    return UserDetailsResponse.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .gender(user.getGender())
        .role(user.getRole().getAccessType())
        .build();
  }

  public UserDetailsResponse registerUser(UserDetailsRequest userDetails) {
    if (userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
      throw new RuntimeException("User with this email already exists.");
    }

    PrepNesterUserDetails userToSave = prepareUserDetails(userDetails);
    PrepNesterUserDetails savedUser = userRepository.save(userToSave);

    return UserDetailsResponse.builder()
        .id(savedUser.getId())
        .fullName(savedUser.getFullName())
        .email(savedUser.getEmail())
        .phoneNumber(savedUser.getPhoneNumber())
        .gender(savedUser.getGender())
        .role(savedUser.getRole().getAccessType())
        .build();
  }

  private PrepNesterUserDetails prepareUserDetails(UserDetailsRequest userDetails) {
    Role role = roleRepository.findByAccessType(AccessType.ADMIN);

    return PrepNesterUserDetails.builder()
        .fullName(userDetails.getFullName())
        .email(userDetails.getEmail())
        .passwordHash(passwordEncoder.encode(userDetails.getPassword()))
        .gender(userDetails.getGender())
        .role(role)
        .build();
  }
}
