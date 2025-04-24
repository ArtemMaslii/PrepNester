package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.QuestionWithoutCategoryDto;
import com.project.prepnester.model.content.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface QuestionToQuestionWithoutCategoryDtoMapper {

  QuestionToQuestionWithoutCategoryDtoMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(
      QuestionToQuestionWithoutCategoryDtoMapper.class);

  @Mapping(source = "id", target = "id")
  QuestionWithoutCategoryDto questionToQuestionWithoutCategoryDto(Question question);
}
