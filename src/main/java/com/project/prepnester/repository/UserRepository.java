package com.project.prepnester.repository;

import com.project.prepnester.model.UserDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Long> {

  Optional<UserDetails> findByEmail(String email);
}
