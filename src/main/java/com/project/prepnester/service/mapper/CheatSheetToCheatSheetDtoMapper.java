package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.CheatSheetDto;
import com.project.prepnester.model.content.CheatSheet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {
        CategoryToCategoryWithQuestionsDtoMapper.class
    }
)
public interface CheatSheetToCheatSheetDtoMapper {

  CheatSheetToCheatSheetDtoMapper INSTANCE = Mappers.getMapper(
      CheatSheetToCheatSheetDtoMapper.class);

  @Mapping(target = "id", source = "id")
  CheatSheetDto cheatSheetToCheatSheetDto(CheatSheet cheatSheet);
}