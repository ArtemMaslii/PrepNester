package com.project.prepnester.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWithQuestionsRequestDto {

  private UUID id;

  private List<QuestionIdsRequestDto> questions;
}