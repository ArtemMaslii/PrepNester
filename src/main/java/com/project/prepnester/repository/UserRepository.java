package com.project.prepnester.repository;

import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PrepNesterUserDetails, UUID> {

  Optional<PrepNesterUserDetails> findByEmail(String email);
}
