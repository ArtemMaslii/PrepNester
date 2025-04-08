package com.project.prepnester.service.impl;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.model.AccessType;
import com.project.prepnester.model.PrepNesterUserDeatils;
import com.project.prepnester.model.Role;
import com.project.prepnester.repository.RoleRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.UserDetailsService;
import com.project.prepnester.service.mapper.UserToUserDetailsResponseMapper;
import com.project.prepnester.util.exceptions.UserDetailsNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  private final UserToUserDetailsResponseMapper userToUserDetailsResponseMapper =
      UserToUserDetailsResponseMapper.INSTANCE;

  @Transactional(readOnly = true)
  public UserDetailsResponse getUserDetails(String email) {
    PrepNesterUserDeatils user = userRepository.findByEmail(email).orElseThrow(
        () -> new UserDetailsNotFoundException("User with email " + email + " not found")
    );

    return userToUserDetailsResponseMapper.prepNesterUserToUserDetailsResponse(user);
  }

  public PrepNesterUserDeatils registerUser(UserDetailsRequest userDetails) {
    PrepNesterUserDeatils userToSave = prepareUserDetails(userDetails);

    return userRepository.save(userToSave);
  }

  private PrepNesterUserDeatils prepareUserDetails(UserDetailsRequest userDetails) {
    Role role = roleRepository.findByAccessType(AccessType.READ_WRITE);

    return PrepNesterUserDeatils.builder()
        .fullName(userDetails.getFullName())
        .email(userDetails.getEmail())
        .passwordHash(passwordEncoder.encode(userDetails.getPasswordHash()))
        .phoneNumber(userDetails.getPhoneNumber())
        .gender(userDetails.getGender())
        .role(role)
        .build();
  }
}
