package com.project.prepnester.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWithQuestionsDto {

  private UUID id;

  private String title;

  private List<QuestionWithoutCategoryDto> questions;
}