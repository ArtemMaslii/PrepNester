package com.project.prepnester.controller;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.model.SortBy;
import com.project.prepnester.service.QuestionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/questions")
@Validated
public class QuestionControllers {

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

  @PostMapping
  public ResponseEntity<QuestionDto> createQuestion(
      @RequestBody @Valid CreateQuestionBodyRequest body) {
    return ResponseEntity.ok(questionService.createQuestion(body));
  }
}
