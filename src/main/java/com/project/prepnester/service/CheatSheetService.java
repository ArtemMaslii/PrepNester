package com.project.prepnester.service;

import com.project.prepnester.dto.request.CheatSheetRequestDto;
import com.project.prepnester.dto.response.CheatSheetDto;
import java.util.List;
import java.util.UUID;

public interface CheatSheetService {

  List<CheatSheetDto> getCheatSheets();

  CheatSheetDto getCheatSheetById(UUID id);

  CheatSheetDto createCheatSheet(CheatSheetRequestDto cheatSheetRequestDto);

  CheatSheetDto updateCheatSheet(UUID id, CheatSheetRequestDto cheatSheetRequestDto);

  void deleteCheatSheet(UUID id);
}
