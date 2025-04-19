package com.project.prepnester.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWithQuestionsDto {

  private String title;
  
  private List<QuestionDto> questions;
}