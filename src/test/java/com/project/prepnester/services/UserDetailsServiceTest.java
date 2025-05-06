package com.project.prepnester.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.Gender;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.model.userDetails.Role;
import com.project.prepnester.repository.RoleRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.UserDetailsService;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @InjectMocks
  private UserDetailsService userDetailsService;

  private PrepNesterUserDetails user;
  private Role candidateRole;

  @BeforeEach
  void setUp() {
    candidateRole = Role.builder()
        .id(UUID.randomUUID())
        .accessType(AccessType.ADMIN)
        .build();

    user = PrepNesterUserDetails.builder()
        .id(UUID.randomUUID())
        .fullName("John Doe")
        .email("john@example.com")
        .passwordHash("encodedPassword")
        .gender(Gender.MALE)
        .role(candidateRole)
        .build();
  }

  @Test
  void testGetUserDetails_Success() {
    // Given
    String email = user.getEmail();
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // When
    UserDetailsResponse result = userDetailsService.getUserDetails(email);

    // Then
    assertNotNull(result);
    assertEquals(user.getEmail(), result.getEmail());
    assertEquals(user.getFullName(), result.getFullName());
    verify(userRepository, times(1)).findByEmail(email);
  }

  @Test
  void testGetUserDetails_UserNotFound() {
    // Given
    String email = "unknown@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(NotFoundException.class, () -> userDetailsService.getUserDetails(email));
    verify(userRepository, times(1)).findByEmail(email);
  }

  @Test
  void testRegisterUser_Success() {
    // Given
    UserDetailsRequest request = new UserDetailsRequest();
    request.setFullName("Jane Doe");
    request.setEmail("jane@example.com");
    request.setPassword("password123");
    request.setGender(Gender.FEMALE);

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
    when(roleRepository.findByAccessType(AccessType.ADMIN)).thenReturn(candidateRole);
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(PrepNesterUserDetails.class))).thenAnswer(invocation -> {
      PrepNesterUserDetails savedUser = invocation.getArgument(0);
      savedUser.setId(UUID.randomUUID());
      return savedUser;
    });

    // When
    UserDetailsResponse result = userDetailsService.registerUser(request);

    // Then
    assertNotNull(result);
    assertEquals(request.getEmail(), result.getEmail());
    assertEquals(request.getFullName(), result.getFullName());
    verify(userRepository, times(1)).findByEmail(request.getEmail());
    verify(roleRepository, times(1)).findByAccessType(AccessType.ADMIN);
    verify(userRepository, times(1)).save(any(PrepNesterUserDetails.class));
  }

  @Test
  void testRegisterUser_UserAlreadyExists() {
    // Given
    UserDetailsRequest request = new UserDetailsRequest();
    request.setEmail(user.getEmail());

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

    // When / Then
    assertThrows(RuntimeException.class, () -> userDetailsService.registerUser(request));
    verify(userRepository, times(1)).findByEmail(request.getEmail());
    verify(roleRepository, never()).findByAccessType(any());
    verify(userRepository, never()).save(any());
  }
}
