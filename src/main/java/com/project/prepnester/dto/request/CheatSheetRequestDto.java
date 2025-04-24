package com.project.prepnester.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheatSheetRequestDto {

  private String title;

  private List<CategoryWithQuestionsRequestDto> categories;
}
