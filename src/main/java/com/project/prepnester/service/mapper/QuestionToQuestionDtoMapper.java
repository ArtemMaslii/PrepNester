package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.model.content.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {
        SubQuestionMapper.class,
        CategoryToCategoryDtoMapper.class
    }
)
public interface QuestionToQuestionDtoMapper {

  QuestionToQuestionDtoMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(
      QuestionToQuestionDtoMapper.class);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "subQuestions", target = "subQuestions")
  @Mapping(source = "category", target = "category")
  QuestionDto questionToQuestionDto(Question question);
}
