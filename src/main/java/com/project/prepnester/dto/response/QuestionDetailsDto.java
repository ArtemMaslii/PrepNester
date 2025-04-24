package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QuestionDetailsDto {

  private UUID id;

  private String title;

  private Boolean isPublic;

  private CategoryDto category;

  private List<SubQuestionWithoutCommentsDto> subQuestions;

  private List<CommentDto> comments;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String createdByName;

  private String updatedByName;
}