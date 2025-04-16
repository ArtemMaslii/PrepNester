package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {

  private UUID id;

  private String title;

  private Boolean isPublic;

  private CategoryDto category;

  private List<SubQuestionDto> subQuestions;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
