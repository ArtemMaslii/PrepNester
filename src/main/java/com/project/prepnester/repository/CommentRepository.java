package com.project.prepnester.repository;

import com.project.prepnester.model.content.Comment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

  List<Comment> findAllByQuestionId(UUID questionId);

  List<Comment> findAllBySubQuestionId(UUID questionId);

  List<Comment> findAllByParentId(UUID parentId);

}
