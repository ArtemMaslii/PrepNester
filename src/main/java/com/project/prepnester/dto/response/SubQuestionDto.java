package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubQuestionDto {

  private UUID id;

  private String title;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
