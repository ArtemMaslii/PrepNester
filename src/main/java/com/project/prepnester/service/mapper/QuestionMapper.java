package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.request.CommentRequestDto;
import com.project.prepnester.dto.response.CommentDto;
import com.project.prepnester.dto.response.CommentQuestionDto;
import com.project.prepnester.dto.response.CommentReplyDto;
import com.project.prepnester.dto.response.CommentSubQuestionDto;
import com.project.prepnester.dto.response.QuestionDetailsDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.dto.response.SubQuestionWithoutCommentsDto;
import com.project.prepnester.model.content.Comment;
import com.project.prepnester.model.content.Like;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.content.SubQuestion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class QuestionMapper {

  public static QuestionDto mapQuestionToDto(Question question, List<Comment> comments,
      List<Like> likes, Boolean isLikedByCurrentUser, List<UUID> likedSubQuestionIds) {
    return QuestionDto.builder()
        .id(question.getId())
        .title(question.getTitle())
        .isPublic(question.getIsPublic())
        .category(CategoryToCategoryDtoMapper.INSTANCE.categoryToCategoryDto(
            question.getCategory()))
        .subQuestions(question.getSubQuestions().stream()
            .map(
                subQuestion -> mapSubQuestionToDtoWithoutComments(subQuestion,
                    likedSubQuestionIds.contains(subQuestion.getId())))
            .toList())
        .commentsCount((long) comments.size())
        .likesCount((long) likes.size())
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .isLikedByCurrentUser(isLikedByCurrentUser)
        .build();
  }

  public static QuestionDetailsDto mapQuestionDetailsToDto(Question question, String createdBy,
      String updatedBy,
      List<Comment> comments) {
    return QuestionDetailsDto.builder()
        .id(question.getId())
        .title(question.getTitle())
        .isPublic(question.getIsPublic())
        .category(CategoryToCategoryDtoMapper.INSTANCE.categoryToCategoryDto(
            question.getCategory()))
        .subQuestions(question.getSubQuestions().stream()
            .map(QuestionMapper::mapSubQuestionToDtoWithoutComments)
            .toList())
        .comments(
            comments.stream()
                .filter(comment -> comment.getParent() == null)
                .map(comment -> CommentDto.builder()
                    .id(comment.getId())
                    .message(comment.getMessage())
                    .questionId(comment.getQuestion() != null
                        ? comment.getQuestion().getId()
                        : null)
                    .likesCount((long) comment.getLikes().size())
                    .createdAt(comment.getCreatedAt())
                    .createdBy(comment.getCreatedBy())
                    .updatedAt(comment.getUpdatedAt())
                    .updatedBy(comment.getUpdatedBy())
                    .replies(
                        comment.getReplies() != null
                            ? comment.getReplies().stream()
                            .map(reply -> CommentReplyDto.builder()
                                .id(reply.getId())
                                .message(reply.getMessage())
                                .questionId(reply.getQuestion() != null
                                    ? reply.getQuestion().getId()
                                    : null)
                                .parentId(reply.getParent() != null
                                    ? reply.getParent().getId()
                                    : null)
                                .likesCount((long) reply.getLikes().size())
                                .createdAt(reply.getCreatedAt())
                                .createdBy(reply.getCreatedBy())
                                .updatedAt(reply.getUpdatedAt())
                                .updatedBy(reply.getUpdatedBy() != null
                                    ? reply.getUpdatedBy()
                                    : null)
                                .build())
                            .toList()
                            : List.of()
                    )
                    .build())
                .toList()
        )
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .createdByName(createdBy)
        .updatedByName(updatedBy)
        .build();
  }

  public static SubQuestionDto mapSubQuestionToDto(SubQuestion subQuestion, String createdBy,
      String updatedBy, List<Comment> comments, List<Like> likes) {
    return SubQuestionDto.builder()
        .id(subQuestion.getId())
        .title(subQuestion.getTitle())
        .comments(
            comments.stream()
                .map(comment -> CommentDto.builder()
                    .id(comment.getId())
                    .message(comment.getMessage())
                    .subQuestionId(comment.getSubQuestion() != null
                        ? comment.getSubQuestion().getId()
                        : null)
                    .likesCount((long) comment.getLikes().size())
                    .createdAt(comment.getCreatedAt())
                    .createdBy(comment.getCreatedBy())
                    .updatedAt(comment.getUpdatedAt())
                    .updatedBy(comment.getUpdatedBy())
                    .replies(
                        comment.getReplies() != null
                            ? comment.getReplies().stream()
                            .map(reply -> CommentReplyDto.builder()
                                .id(reply.getId())
                                .message(reply.getMessage())
                                .subQuestionId(reply.getSubQuestion() != null
                                    ? reply.getSubQuestion().getId()
                                    : null)
                                .parentId(reply.getParent() != null
                                    ? reply.getParent().getId()
                                    : null)
                                .likesCount((long) reply.getLikes().size())
                                .createdAt(reply.getCreatedAt())
                                .createdBy(reply.getCreatedBy())
                                .updatedAt(reply.getUpdatedAt())
                                .updatedBy(reply.getUpdatedBy() != null
                                    ? reply.getUpdatedBy()
                                    : null)
                                .build())
                            .toList()
                            : List.of()
                    )
                    .build())
                .toList())
        .likesCount((long) likes.size())
        .createdAt(subQuestion.getCreatedAt())
        .createdBy(createdBy)
        .updatedAt(subQuestion.getUpdatedAt())
        .updatedBy(updatedBy)
        .build();
  }

  public static SubQuestionWithoutCommentsDto mapSubQuestionToDtoWithoutComments(
      SubQuestion subQuestion) {
    return SubQuestionWithoutCommentsDto.builder()
        .id(subQuestion.getId())
        .title(subQuestion.getTitle())
        .commentsCount((long) subQuestion.getComments().size())
        .likesCount((long) subQuestion.getLikes().size())
        .createdAt(subQuestion.getCreatedAt())
        .updatedAt(subQuestion.getUpdatedAt())
        .build();
  }

  public static SubQuestionWithoutCommentsDto mapSubQuestionToDtoWithoutComments(
      SubQuestion subQuestion, Boolean isLikedByCurrentUser) {
    return SubQuestionWithoutCommentsDto.builder()
        .id(subQuestion.getId())
        .title(subQuestion.getTitle())
        .commentsCount((long) subQuestion.getComments().size())
        .likesCount((long) subQuestion.getLikes().size())
        .createdAt(subQuestion.getCreatedAt())
        .updatedAt(subQuestion.getUpdatedAt())
        .isLikedByCurrentUser(isLikedByCurrentUser)
        .build();
  }

  public static Comment mapDtoToComment(CommentRequestDto commentDto) {
    return Comment.builder()
        .message(commentDto.getMessage())
        .createdAt(LocalDateTime.now())
        .createdBy(commentDto.getCreatedBy())
        .updatedAt(null)
        .updatedBy(null)
        .build();
  }

  public static CommentSubQuestionDto mapSubQuestionCommentToReplyDto(Comment comment) {
    return CommentSubQuestionDto.builder()
        .id(comment.getId())
        .message(comment.getMessage())
        .subQuestionId(comment.getSubQuestion() != null
            ? comment.getSubQuestion().getId()
            : null)
        .parentId(comment.getParent() != null
            ? comment.getParent().getId()
            : null)
        .likesCount((long) comment.getLikes().size())
        .createdAt(comment.getCreatedAt())
        .createdBy(comment.getCreatedBy())
        .updatedAt(comment.getUpdatedAt())
        .updatedBy(comment.getUpdatedBy())
        .build();
  }

  public static CommentQuestionDto mapQuestionCommentToReplyDto(Comment comment) {
    return CommentQuestionDto.builder()
        .id(comment.getId())
        .message(comment.getMessage())
        .questionId(comment.getQuestion() != null
            ? comment.getQuestion().getId()
            : null)
        .parentId(comment.getParent() != null
            ? comment.getParent().getId()
            : null)
        .likesCount((long) comment.getLikes().size())
        .createdAt(comment.getCreatedAt())
        .createdBy(comment.getCreatedBy())
        .updatedAt(comment.getUpdatedAt())
        .updatedBy(comment.getUpdatedBy())
        .build();
  }
}
