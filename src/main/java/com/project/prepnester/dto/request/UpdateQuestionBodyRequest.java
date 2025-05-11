package com.project.prepnester.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateQuestionBodyRequest {

  @NotBlank(message = "Title is required")
  @NotEmpty(message = "Title cannot be empty")
  private String title;
  private UUID createdBy;
}
