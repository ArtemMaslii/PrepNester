package com.project.prepnester.repository;

import com.project.prepnester.model.content.Like;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, UUID> {

  List<Like> findAllByQuestionId(UUID questionId);

  List<Like> findAllBySubQuestionId(UUID subQuestionId);

  List<Like> findAllByCheatSheetId(UUID cheatSheetId);

  List<Like> findAllByCommentId(UUID commentId);

  Like findByQuestionIdAndUserId(UUID questionId, UUID userId);

  Like findByCheatSheetIdAndUserId(UUID cheatSheetId, UUID userId);

  Like findBySubQuestionIdAndUserId(UUID subQuestionId, UUID userId);

  Like findByCommentIdAndUserId(UUID commentId, UUID userId);

  void deleteByQuestionIdAndUserId(UUID questionId, UUID userId);

  void deleteByCheatSheetIdAndUserId(UUID cheatSheetId, UUID userId);

  void deleteBySubQuestionIdAndUserId(UUID subQuestionId, UUID userId);

  void deleteByCommentIdAndUserId(UUID commentId, UUID userId);

  Boolean existsByCheatSheetIdAndUserId(UUID cheatSheetId, UUID userId);

  Boolean existsByQuestionIdAndUserId(UUID questionId, UUID userId);

  Boolean existsBySubQuestionIdAndUserId(UUID subQuestionId, UUID userId);

  Boolean existsByCommentIdAndUserId(UUID commentId, UUID userId);
}
