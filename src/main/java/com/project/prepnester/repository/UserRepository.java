package com.project.prepnester.repository;

import com.project.prepnester.model.userDetails.PrepNesterUserDeatils;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PrepNesterUserDeatils, Long> {

  Optional<PrepNesterUserDeatils> findByEmail(String email);
}
