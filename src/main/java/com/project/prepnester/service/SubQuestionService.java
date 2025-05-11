package com.project.prepnester.service;

import static com.project.prepnester.service.mapper.QuestionMapper.mapSubQuestionToDto;

import com.project.prepnester.dto.request.SubQuestionDtoRequest;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.model.content.Comment;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class SubQuestionService {

  private final SubQuestionRepository subQuestionRepository;

  private final UserIdService userIdService;

  private final UserRepository userRepository;

  private final CommentRepository commentRepository;

  private final LikeRepository likeRepository;

  @Transactional(readOnly = true)
  public SubQuestionDto getSubQuestionById(UUID subQuestionId) {
    log.info("Fetching sub-questions for question with id: {}", subQuestionId);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub question not found"));

    String createdBy = userRepository.findById(subQuestion.getCreatedBy())
        .orElseThrow(
            () -> new NotFoundException(
                "User with id " + subQuestion.getCreatedBy() + " not found"))
        .getFullName();

    String updatedBy = null;

    if (subQuestion.getUpdatedBy() != null) {
      updatedBy = userRepository.findById(subQuestion.getUpdatedBy())
          .orElseThrow(
              () -> new NotFoundException(
                  "User with id " + subQuestion.getUpdatedBy() + " not found"))
          .getFullName();
    }

    List<Comment> comments = commentRepository.findAllByQuestionId(subQuestionId);

    Map<UUID, String> userNamesByIdCreated = comments.stream()
        .collect(Collectors.toMap(Comment::getCreatedBy, comment -> {
          PrepNesterUserDetails user = userRepository.findById(comment.getCreatedBy())
              .orElseThrow(() -> new NotFoundException("User not found"));
          return user.getFullName();
        }));

    Map<UUID, String> userNamesByIdUpdated = comments.stream()
        .collect(Collectors.toMap(Comment::getCreatedBy, comment -> {
          PrepNesterUserDetails user = userRepository.findById(comment.getCreatedBy())
              .orElseThrow(() -> new NotFoundException("User not found"));
          return user.getFullName();
        }));

    Map<UUID, Boolean> likesCommentIds = comments.stream()
        .collect(Collectors.toMap(Comment::getId,
            comment -> likeRepository.existsByCommentIdAndUserId(comment.getId(),
                userIdService.getCurrentUserId())));

    return mapSubQuestionToDto(subQuestion, createdBy, updatedBy,
        commentRepository.findAllBySubQuestionId(subQuestionId),
        userNamesByIdCreated,
        userNamesByIdUpdated,
        likeRepository.findAllBySubQuestionId(
            subQuestionId),
        likesCommentIds);
  }

  public SubQuestionDto updateSubQuestion(UUID subQuestionId,
      SubQuestionDtoRequest request) {
    log.info("Updating question with id: {} and title: {}", subQuestionId, request);

    if (!userIdService.getCurrentUserId().equals(request.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub-question not found"));

    subQuestion.setTitle(request.getTitle());
    subQuestion.setUpdatedAt(LocalDateTime.now());

    String createdBy = userRepository.findById(subQuestion.getCreatedBy())
        .orElseThrow(
            () -> new NotFoundException(
                "User with id " + subQuestion.getCreatedBy() + " not found"))
        .getFullName();

    String updatedBy = null;

    if (subQuestion.getUpdatedBy() != null) {
      updatedBy = userRepository.findById(subQuestion.getUpdatedBy())
          .orElseThrow(
              () -> new NotFoundException(
                  "User with id " + subQuestion.getUpdatedBy() + " not found"))
          .getFullName();
    }

    List<Comment> comments = commentRepository.findAllByQuestionId(subQuestionId);

    Map<UUID, String> userNamesByIdCreated = comments.stream()
        .collect(Collectors.toMap(Comment::getCreatedBy, comment -> {
          PrepNesterUserDetails user = userRepository.findById(comment.getCreatedBy())
              .orElseThrow(() -> new NotFoundException("User not found"));
          return user.getFullName();
        }));

    Map<UUID, String> userNamesByIdUpdated = comments.stream()
        .collect(Collectors.toMap(Comment::getCreatedBy, comment -> {
          PrepNesterUserDetails user = userRepository.findById(comment.getCreatedBy())
              .orElseThrow(() -> new NotFoundException("User not found"));
          return user.getFullName();
        }));

    Map<UUID, Boolean> likesCommentIds = comments.stream()
        .collect(Collectors.toMap(Comment::getId,
            comment -> likeRepository.existsByCommentIdAndUserId(comment.getId(),
                userIdService.getCurrentUserId())));

    return mapSubQuestionToDto(subQuestion, createdBy, updatedBy,
        commentRepository.findAllBySubQuestionId(subQuestionId),
        userNamesByIdCreated,
        userNamesByIdUpdated,
        likeRepository.findAllBySubQuestionId(
            subQuestionId),
        likesCommentIds);
  }

  public void deleteSubQuestion(UUID subQuestionId) {
    log.info("Deleting sub-question with id: {}", subQuestionId);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub-question not found"));

    if (!userIdService.getCurrentUserId().equals(subQuestion.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    subQuestionRepository.delete(subQuestion);
  }

}
