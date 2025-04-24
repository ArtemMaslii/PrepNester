package com.project.prepnester.service;

import static com.project.prepnester.service.mapper.QuestionMapper.mapDtoToComment;
import static com.project.prepnester.service.mapper.QuestionMapper.mapQuestionCommentToReplyDto;
import static com.project.prepnester.service.mapper.QuestionMapper.mapSubQuestionCommentToReplyDto;

import com.project.prepnester.dto.request.CommentRequestDto;
import com.project.prepnester.dto.request.CommentUpdateRequestDto;
import com.project.prepnester.dto.response.CommentQuestionDto;
import com.project.prepnester.dto.response.CommentSubQuestionDto;
import com.project.prepnester.model.content.Comment;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CommentService {

  private final QuestionRepository questionRepository;

  private final SubQuestionRepository subQuestionRepository;

  private final CommentRepository commentRepository;

  private final UserIdService userIdService;

  public CommentSubQuestionDto createSubQuestionComment(UUID subQuestionId,
      CommentRequestDto body) {
    log.info("Creating comment from body: {}", body);

    Comment commentToSave = mapDtoToComment(body);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub question not found"));
    commentToSave.setSubQuestion(subQuestion);

    if (body.getParentId() != null) {
      Comment parentComment = commentRepository.findById(body.getParentId())
          .orElseThrow(() -> new NotFoundException("Parent comment not found"));

      if (parentComment.getParent() != null) {
        throw new IllegalArgumentException("Parent comment cannot be a reply to another comment");
      }
      commentToSave.setParent(parentComment);
    }

    Comment saved = commentRepository.save(commentToSave);

    return mapSubQuestionCommentToReplyDto(saved);
  }


  public CommentQuestionDto createQuestionComment(UUID questionId,
      CommentRequestDto body) {
    log.info("Creating comment from body: {}", body);

    Comment commentToSave = mapDtoToComment(body);

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new NotFoundException("Sub question not found"));
    commentToSave.setQuestion(question);

    if (body.getParentId() != null) {
      Comment parentComment = commentRepository.findById(body.getParentId())
          .orElseThrow(() -> new NotFoundException("Parent comment not found"));

      if (parentComment.getParent() != null) {
        throw new IllegalArgumentException("Parent comment cannot be a reply to another comment");
      }
      commentToSave.setParent(parentComment);
    }

    Comment saved = commentRepository.save(commentToSave);

    return mapQuestionCommentToReplyDto(saved);
  }

  public CommentSubQuestionDto updateSubQuestionComment(UUID commentId,
      CommentUpdateRequestDto body) {
    log.info("Updating comment with id: {} from body: {}", commentId, body);

    if (!body.getCreatedBy().equals(body.getUpdatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException("Comment not found"));

    comment.setMessage(body.getMessage());
    comment.setUpdatedBy(body.getUpdatedBy());

    return mapSubQuestionCommentToReplyDto(commentRepository.save(comment));
  }


  public CommentQuestionDto updateQuestionComment(UUID commentId,
      CommentUpdateRequestDto body) {
    log.info("Updating comment with id: {} from body: {}", commentId, body);

    if (!body.getCreatedBy().equals(body.getUpdatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException("Comment not found"));

    comment.setMessage(body.getMessage());
    comment.setUpdatedBy(body.getUpdatedBy());

    return mapQuestionCommentToReplyDto(commentRepository.save(comment));
  }

  public void deleteComment(UUID commentId) {
    log.info("Deleting comment with id: {}", commentId);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException("Comment not found"));

    List<Comment> commentsToDelete = new ArrayList<>();

    if (comment.getParent() != null) {
      commentsToDelete = commentRepository.findAllByParentId(
          comment.getParent().getId());
    }

    if (!userIdService.getCurrentUserId().equals(comment.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    commentRepository.delete(comment);
    commentRepository.deleteAll(commentsToDelete);
  }
}
