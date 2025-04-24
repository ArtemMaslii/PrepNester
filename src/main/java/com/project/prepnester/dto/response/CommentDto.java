package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {

  private UUID id;

  private String message;

  private UUID questionId;

  private UUID subQuestionId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private UUID createdBy;

  private UUID updatedBy;

  private List<CommentReplyDto> replies;
}
