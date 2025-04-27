package com.project.prepnester.service;

import com.project.prepnester.model.content.Like;
import com.project.prepnester.repository.CheatSheetRepository;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class LikeService {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final QuestionRepository questionRepository;
  private final SubQuestionRepository subQuestionRepository;
  private final CheatSheetRepository cheatSheetRepository;
  private final CommentRepository commentRepository;

  public void likeQuestion(UUID questionId, UUID userId) {
    Like like = likeRepository.findByQuestionIdAndUserId(questionId, userId);

    if (like != null) {
      throw new IllegalArgumentException("User already liked this question");
    }

    Like likeToSave = Like.builder()
        .user(userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found")))
        .question(questionRepository.findById(questionId)
            .orElseThrow(() -> new NotFoundException("Question not found")))
        .build();

    likeRepository.save(likeToSave);
  }

  public void likeSubQuestion(UUID subQuestionId, UUID userId) {
    Like like = likeRepository.findBySubQuestionIdAndUserId(subQuestionId, userId);

    if (like != null) {
      throw new IllegalArgumentException("User already liked this sub question");
    }

    Like likeToSave = Like.builder()
        .user(userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found")))
        .subQuestion(subQuestionRepository.findById(subQuestionId)
            .orElseThrow(() -> new NotFoundException("Sub question not found")))
        .build();

    likeRepository.save(likeToSave);
  }

  public void likeCheatSheet(UUID cheatSheetId, UUID userId) {
    Like like = likeRepository.findByCheatSheetIdAndUserId(cheatSheetId, userId);

    if (like != null) {
      throw new IllegalArgumentException("User already liked this cheat sheet");
    }

    Like likeToSave = Like.builder()
        .user(userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found")))
        .cheatSheet(cheatSheetRepository.findById(cheatSheetId)
            .orElseThrow(() -> new NotFoundException("Cheat sheet not found")))
        .build();

    likeRepository.save(likeToSave);
  }

  public void likeComment(UUID commentId, UUID userId) {
    Like like = likeRepository.findByCommentIdAndUserId(commentId, userId);

    if (like != null) {
      throw new IllegalArgumentException("User already liked this comment");
    }

    Like likeToSave = Like.builder()
        .user(userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found")))
        .comment(commentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException("Comment not found")))
        .build();

    likeRepository.save(likeToSave);
  }

  public void removeQuestionLike(UUID questionId, UUID userId) {
    Like like = likeRepository.findByQuestionIdAndUserId(questionId, userId);

    if (like == null) {
      throw new IllegalArgumentException("User hasn't liked this question yet");
    }

    likeRepository.deleteByQuestionIdAndUserId(questionId, userId);
  }

  public void removeSubQuestionLike(UUID subQuestionId, UUID userId) {
    Like like = likeRepository.findBySubQuestionIdAndUserId(subQuestionId, userId);

    if (like == null) {
      throw new IllegalArgumentException("User hasn't liked this sub question yet");
    }

    likeRepository.deleteBySubQuestionIdAndUserId(subQuestionId, userId);
  }

  public void removeCheatSheetLike(UUID cheatSheetId, UUID userId) {
    Like like = likeRepository.findByCheatSheetIdAndUserId(cheatSheetId, userId);

    if (like == null) {
      throw new IllegalArgumentException("User hasn't liked this cheat sheet yet");
    }

    likeRepository.deleteByCheatSheetIdAndUserId(cheatSheetId, userId);
  }

  public void removeCommentLike(UUID commentId, UUID userId) {
    Like like = likeRepository.findByCommentIdAndUserId(commentId, userId);

    if (like == null) {
      throw new IllegalArgumentException("User hasn't liked this comment yet");
    }

    likeRepository.deleteByCommentIdAndUserId(commentId, userId);
  }
}
