package com.project.prepnester.service;

import com.project.prepnester.model.interview.Candidate;
import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.CandidateRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserJwtService implements UserDetailsService {

  private final UserRepository userRepository;

  private final CandidateRepository candidateRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    Optional<PrepNesterUserDetails> user = userRepository.findByEmail(email);

    Optional<Candidate> candidate = candidateRepository.findByEmail(email);

    if (user.isEmpty() && candidate.isEmpty()) {
      throw new NotFoundException("User not found");
    }

    if (user.isPresent()) {
      Set<GrantedAuthority> authorities = Stream.of(user.get().getRole())
          .map((role) -> new SimpleGrantedAuthority(role.getAccessType().getValue())).collect(
              Collectors.toSet());

      return new User(
          email,
          user.get().getPasswordHash(),
          authorities
      );
    }

    Set<GrantedAuthority> authorities = Set.of(
        new SimpleGrantedAuthority(AccessType.GUEST.getValue()));

    return new User(
        email,
        candidate.get().getPasswordHash(),
        authorities
    );
  }
}
