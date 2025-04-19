package com.project.prepnester.dto.request;

import com.project.prepnester.dto.response.CategoryWithQuestionsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheatSheetRequestDto {

  private String title;

  private List<CategoryWithQuestionsDto> categories;

  private String createdAt;
}
