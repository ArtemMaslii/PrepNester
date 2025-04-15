package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.CategoryDto;
import com.project.prepnester.model.content.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryToCategoryDtoMapper {

  CategoryToCategoryDtoMapper INSTANCE = Mappers.getMapper(
      CategoryToCategoryDtoMapper.class);

  @Mapping(source = "title", target = "title")
  CategoryDto categoryToCategoryDto(Category category);
}