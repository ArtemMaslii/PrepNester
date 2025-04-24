package com.project.prepnester.service;

import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserJwtService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    PrepNesterUserDetails user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    Set<GrantedAuthority> authorities = Stream.of(user.getRole())
        .map((role) -> new SimpleGrantedAuthority(role.getAccessType().getValue())).collect(
            Collectors.toSet());

    return new User(
        email,
        user.getPasswordHash(),
        authorities
    );
  }
}
