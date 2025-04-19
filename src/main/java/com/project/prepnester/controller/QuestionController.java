package com.project.prepnester.controller;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.SubQuestionDtoRequest;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.service.QuestionService;
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
  public ResponseEntity<QuestionDto> getQuestionById(@PathVariable UUID questionId) {

    QuestionDto question = questionService.getQuestionById(questionId);
    return ResponseEntity.ok(question);
  }

  @GetMapping("/sub-questions/{subQuestionId}")
  public ResponseEntity<SubQuestionDto> getSubQuestionsByQuestionId(
      @PathVariable UUID subQuestionId) {

    SubQuestionDto subQuestion = questionService.getSubQuestionById(subQuestionId);
    return ResponseEntity.ok(subQuestion);
  }

  @PostMapping
  public ResponseEntity<QuestionDto> createQuestion(
      @RequestBody @Valid CreateQuestionBodyRequest body) {

    return ResponseEntity.ok(questionService.createQuestion(body));
  }

  @PutMapping("/{questionId}")
  public ResponseEntity<QuestionDto> updateQuestion(@PathVariable UUID questionId,
      @RequestBody @Valid CreateQuestionBodyRequest body) {

    QuestionDto updatedQuestion = questionService.updateQuestion(questionId, body);
    return ResponseEntity.ok(updatedQuestion);
  }

  @PutMapping("/sub-questions/{subQuestionId}")
  public ResponseEntity<SubQuestionDto> updateSubQuestion(@PathVariable UUID subQuestionId,
      @RequestBody SubQuestionDtoRequest subQuestionDtoRequest) {

    SubQuestionDto updatedSubQuestion = questionService.updateSubQuestion(subQuestionId,
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
    questionService.deleteSubQuestion(subQuestionId);
    return ResponseEntity.noContent().build();
  }
}
