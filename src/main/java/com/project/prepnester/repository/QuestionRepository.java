package com.project.prepnester.repository;

import com.project.prepnester.model.content.Question;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

  List<Question> findAllByIsPublic(Boolean isPublic, Pageable pageable);

  @Query("""
          SELECT DISTINCT q FROM Question q
          LEFT JOIN q.subQuestions sub
          WHERE q.isPublic = :isPublic
            AND (
              LOWER(q.title) LIKE LOWER(CONCAT('%', :search, '%')) 
              OR LOWER(sub.title) LIKE LOWER(CONCAT('%', :search, '%'))
            )
      """)
  List<Question> findAllByIsPublicAndTitleOrSubTitleContains(@Param("isPublic") Boolean isPublic,
      @Param("search") String search);
}
