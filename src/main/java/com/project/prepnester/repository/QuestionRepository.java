package com.project.prepnester.repository;

import com.project.prepnester.model.content.Question;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

  List<Question> findAllByIsPublic(Boolean isPublic, Pageable pageable);
}
