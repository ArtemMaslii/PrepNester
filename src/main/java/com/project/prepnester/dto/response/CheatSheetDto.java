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
public class CheatSheetDto {

  private UUID id;

  private String title;

  private Boolean isPublic;

  private List<CategoryWithQuestionsDto> categories;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Long likesCount = 0L;

  private UUID createdBy;

  private UUID updatedBy;
}
