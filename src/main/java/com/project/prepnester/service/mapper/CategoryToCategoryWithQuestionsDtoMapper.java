package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.CategoryWithQuestionsDto;
import com.project.prepnester.model.content.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryToCategoryWithQuestionsDtoMapper {

  CategoryToCategoryWithQuestionsDtoMapper INSTANCE = Mappers.getMapper(
      CategoryToCategoryWithQuestionsDtoMapper.class);

  @Mapping(source = "title", target = "title")
  CategoryWithQuestionsDto categoryToCategoryWithQuestionsDto(Category category);
}