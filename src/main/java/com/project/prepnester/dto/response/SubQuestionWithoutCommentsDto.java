package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class SubQuestionWithoutCommentsDto {

  private UUID id;

  private String title;

  private Long commentsCount = 0L;

  private Long likesCount = 0L;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
