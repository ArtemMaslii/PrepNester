package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheatSheetPreview {

  private UUID id;

  private String title;

  private Boolean isPublic;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Long likesCount = 0L;

  private Long commentsCount = 0L;

  private UUID createdBy;

  private UUID updatedBy;

  private Boolean isLikedByCurrentUser;
}

