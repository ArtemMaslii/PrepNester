package com.project.prepnester.service.impl;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.model.userDetails.Role;
import com.project.prepnester.repository.RoleRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.UserDetailsService;
import com.project.prepnester.service.mapper.UserToUserDetailsResponseMapper;
import com.project.prepnester.util.exceptions.UserDetailsNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  private final UserToUserDetailsResponseMapper userToUserDetailsResponseMapper =
      UserToUserDetailsResponseMapper.INSTANCE;

  @Transactional(readOnly = true)
  public UserDetailsResponse getUserDetails(String email) {
    PrepNesterUserDetails user = userRepository.findByEmail(email).orElseThrow(
        () -> new UserDetailsNotFoundException("User with email " + email + " not found")
    );

    return userToUserDetailsResponseMapper.prepNesterUserToUserDetailsResponse(user);
  }

  public UserDetailsResponse registerUser(UserDetailsRequest userDetails) {
    if (userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
      throw new RuntimeException("User with this email already exists.");
    }

    PrepNesterUserDetails userToSave = prepareUserDetails(userDetails);
    PrepNesterUserDetails savedUser = userRepository.save(userToSave);

    return userToUserDetailsResponseMapper.prepNesterUserToUserDetailsResponse(savedUser);
  }

  private PrepNesterUserDetails prepareUserDetails(UserDetailsRequest userDetails) {
    Role role = roleRepository.findByAccessType(AccessType.CANDIDATE);

    return PrepNesterUserDetails.builder()
        .fullName(userDetails.getFullName())
        .email(userDetails.getEmail())
        .passwordHash(passwordEncoder.encode(userDetails.getPassword()))
        .phoneNumber(userDetails.getPhoneNumber())
        .gender(userDetails.getGender())
        .role(role)
        .build();
  }
}
