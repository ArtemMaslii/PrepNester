package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionWithoutCategoryDto {

  private UUID id;

  private String title;

  private Boolean isPublic;

  private List<SubQuestionWithoutCommentsDto> subQuestions;

  private Long commentsCount = 0L;

  private Long likesCount = 0L;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
