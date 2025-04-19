package com.project.prepnester.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheatSheetDto {

  private UUID id;

  private String title;

  private List<CategoryWithQuestionsDto> categories;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private UUID createdBy;

  private UUID updatedBy;
}
