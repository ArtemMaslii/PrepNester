package com.project.prepnester.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheatSheetRequestDto {

  private String title;

  private Boolean isPublic;

  private List<CategoryWithQuestionsRequestDto> categories;
}
