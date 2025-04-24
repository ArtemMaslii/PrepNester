package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.List;
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
public class SubQuestionDto {

  private UUID id;

  private String title;

  private List<CommentDto> comments;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String createdBy;

  private String updatedBy;
}
