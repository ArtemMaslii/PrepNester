package com.project.prepnester.dto.request;

import com.project.prepnester.dto.response.CategoryDto;
import com.project.prepnester.dto.response.SubQuestionDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateQuestionBodyRequest {

  @NotBlank(message = "Title is required")
  @NotEmpty(message = "Title cannot be empty")
  private String title;
  @NotNull(message = "Description is required")
  private CategoryDto category;
  @NotNull(message = "isPublic flag is required")
  private Boolean isPublic;
  private List<SubQuestionDto> subQuestions;
}