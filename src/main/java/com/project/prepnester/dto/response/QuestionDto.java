package com.project.prepnester.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {

  private String title;

  private Boolean isPublic;

  private CategoryDto category;

  private List<SubQuestionDto> subQuestions;
}
