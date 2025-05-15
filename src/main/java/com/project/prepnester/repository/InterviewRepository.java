package com.project.prepnester.repository;

import com.project.prepnester.model.interview.Interview;
import com.project.prepnester.model.interview.Status;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {

  List<Interview> findAllByCandidate_FullNameContainingIgnoreCaseAndStatus(String search,
      Status status);

  List<Interview> findAllByCandidate_FullNameContainingIgnoreCase(String search);

  List<Interview> findAllByStatus(Status status);

}
