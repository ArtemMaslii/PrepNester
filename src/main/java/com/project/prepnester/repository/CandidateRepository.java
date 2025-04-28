package com.project.prepnester.repository;

import com.project.prepnester.model.interview.Candidate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

  Optional<Candidate> findByEmail(String email);

}
