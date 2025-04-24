package com.project.prepnester.controller;

import com.project.prepnester.dto.request.CommentRequestDto;
import com.project.prepnester.dto.request.CommentUpdateRequestDto;
import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.SubQuestionDtoRequest;
import com.project.prepnester.dto.request.UpdateQuestionBodyRequest;
import com.project.prepnester.dto.response.CommentQuestionDto;
import com.project.prepnester.dto.response.CommentSubQuestionDto;
import com.project.prepnester.dto.response.QuestionDetailsDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.dto.response.SubQuestionWithoutCommentsDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.service.CommentService;
import com.project.prepnester.service.QuestionService;
import com.project.prepnester.service.SubQuestionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/questions")
@Slf4j
@Validated
public class QuestionController {

  private final QuestionService questionService;

  private final SubQuestionService subQuestionService;

  private final CommentService commentService;

  @GetMapping
  public List<QuestionDto> getAllQuestions(
      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
      @RequestParam(required = false, defaultValue = "25") Integer pageSize,
      @RequestParam(required = false, defaultValue = "true") Boolean isPublic,
      @RequestParam(required = false, defaultValue = "ASCENDING") SortBy sortBy) {

    return questionService.getAllQuestions(
        new PageInfoDto(pageNum, pageSize),
        sortBy,
        isPublic
    );
  }

  @GetMapping("/{questionId}")
  public ResponseEntity<QuestionDetailsDto> getQuestionById(@PathVariable UUID questionId) {

    QuestionDetailsDto question = questionService.getQuestionById(questionId);
    return ResponseEntity.ok(question);
  }

  @GetMapping("/sub-questions/{subQuestionId}")
  public ResponseEntity<SubQuestionDto> getSubQuestionsByQuestionId(
      @PathVariable UUID subQuestionId) {

    SubQuestionDto subQuestion = subQuestionService.getSubQuestionById(subQuestionId);
    return ResponseEntity.ok(subQuestion);
  }

  @PostMapping
  public ResponseEntity<QuestionDto> createQuestion(
      @RequestBody @Valid CreateQuestionBodyRequest body) {

    return ResponseEntity.ok(questionService.createQuestion(body));
  }

  @PutMapping("/{questionId}")
  public ResponseEntity<QuestionDto> updateQuestion(@PathVariable UUID questionId,
      @RequestBody @Valid UpdateQuestionBodyRequest body) {

    QuestionDto updatedQuestion = questionService.updateQuestion(questionId, body);
    return ResponseEntity.ok(updatedQuestion);
  }

  @PutMapping("/sub-questions/{subQuestionId}")
  public ResponseEntity<SubQuestionWithoutCommentsDto> updateSubQuestion(
      @PathVariable UUID subQuestionId,
      @RequestBody SubQuestionDtoRequest subQuestionDtoRequest) {

    SubQuestionWithoutCommentsDto updatedSubQuestion = subQuestionService.updateSubQuestion(
        subQuestionId,
        subQuestionDtoRequest);
    return ResponseEntity.ok(updatedSubQuestion);
  }

  @DeleteMapping("/{questionId}")
  public ResponseEntity<Void> deleteQuestion(@PathVariable UUID questionId) {

    questionService.deleteQuestion(questionId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/sub-questions/{subQuestionId}")
  public ResponseEntity<Void> deleteSubQuestion(@PathVariable UUID subQuestionId) {
    subQuestionService.deleteSubQuestion(subQuestionId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/sub-questions/{subQuestionId}/comments")
  public ResponseEntity<CommentSubQuestionDto> createComment(@PathVariable UUID subQuestionId,
      @RequestBody @Valid CommentRequestDto commentRequestDto) {
    CommentSubQuestionDto response = commentService.createSubQuestionComment(subQuestionId,
        commentRequestDto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{questionId}/comments")
  public ResponseEntity<CommentQuestionDto> createCommentToQuestion(@PathVariable UUID questionId,
      @RequestBody @Valid CommentRequestDto commentRequestDto) {
    CommentQuestionDto response = commentService.createQuestionComment(questionId,
        commentRequestDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/sub-questions/comments/{commentId}")
  public ResponseEntity<CommentSubQuestionDto> updateComment(@PathVariable UUID commentId,
      @RequestBody @Valid CommentUpdateRequestDto commentRequestDto) {
    CommentSubQuestionDto response = commentService.updateSubQuestionComment(commentId,
        commentRequestDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/comments/{commentId}")
  public ResponseEntity<CommentQuestionDto> updateCommentToQuestion(@PathVariable UUID commentId,
      @RequestBody @Valid CommentUpdateRequestDto commentRequestDto) {
    CommentQuestionDto response = commentService.updateQuestionComment(commentId,
        commentRequestDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
    commentService.deleteComment(commentId);
    return ResponseEntity.noContent().build();
  }
}
