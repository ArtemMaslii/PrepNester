package com.project.prepnester.dto.request;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubQuestionDtoRequest {

  private String title;
  private UUID createdBy;
}
