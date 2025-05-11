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

  private Long likesCount = 0L;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private UUID createdBy;

  private String createdByName;

  private UUID updatedBy;

  private String updatedByName;

  private List<CommentReplyDto> replies;

  private boolean isLikedByCurrentUser;
}
