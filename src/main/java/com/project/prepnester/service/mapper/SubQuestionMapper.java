package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.model.content.SubQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubQuestionMapper {

  SubQuestionMapper INSTANCE = Mappers.getMapper(
      SubQuestionMapper.class);

  @Mapping(source = "id", target = "id")
  SubQuestionDto subQuestionToSubQuestionDto(SubQuestion subQuestion);

  @Mapping(source = "id", target = "id")
  SubQuestion subQuestionDtoToSubQuestion(SubQuestionDto subQuestionDto);
}