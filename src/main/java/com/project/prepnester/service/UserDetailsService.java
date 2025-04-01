package com.project.prepnester.service;

import com.project.prepnester.dto.UserDetailsDto;
import com.project.prepnester.model.AccessType;
import com.project.prepnester.model.Role;
import com.project.prepnester.model.UserDetails;
import com.project.prepnester.repository.RoleRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.UserDetailsNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public UserDetails getUserDetails(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new UserDetailsNotFoundException("User with email " + email + " not found")
    );
  }

  public UserDetails registerUser(UserDetailsDto userDetails) {
    UserDetails userToSave = prepareUserDetails(userDetails);

    return userRepository.save(userToSave);
  }

  private UserDetails prepareUserDetails(UserDetailsDto userDetails) {
    UserDetails userToSave = new UserDetails();
    userToSave.setFullName(userDetails.getFullName());
    userToSave.setEmail(userDetails.getEmail());
    userToSave.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
    userToSave.setPhoneNumber(userDetails.getPhoneNumber());
    userToSave.setGender(userDetails.getGender());

    Role role = roleRepository.findByAccessType(AccessType.READ_WRITE);
    userToSave.setRole(role);

    return userToSave;
  }
}
