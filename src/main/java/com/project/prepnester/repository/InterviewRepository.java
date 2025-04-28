package com.project.prepnester.repository;

import com.project.prepnester.model.interview.Interview;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {

}
