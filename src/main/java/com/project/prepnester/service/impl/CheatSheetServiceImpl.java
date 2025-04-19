package com.project.prepnester.service.impl;

import com.project.prepnester.dto.request.CheatSheetRequestDto;
import com.project.prepnester.dto.response.CheatSheetDto;
import com.project.prepnester.model.content.CheatSheet;
import com.project.prepnester.repository.CheatSheetRepository;
import com.project.prepnester.service.CheatSheetService;
import com.project.prepnester.service.mapper.CheatSheetToCheatSheetDtoMapper;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CheatSheetServiceImpl implements CheatSheetService {

  private final CheatSheetRepository cheatSheetRepository;

  @Override
  public List<CheatSheetDto> getCheatSheets() {
    log.info("Fetching all cheat sheets from the database");

    return cheatSheetRepository.findAll()
        .stream()
        .map(CheatSheetToCheatSheetDtoMapper.INSTANCE::cheatSheetToCheatSheetDto)
        .toList();
  }

  @Override
  public CheatSheetDto getCheatSheetById(UUID id) {
    log.info("Fetching cheat sheet with id: {}", id);

    CheatSheet cheatSheet = cheatSheetRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cheat sheet not found"));

    return CheatSheetToCheatSheetDtoMapper.INSTANCE.cheatSheetToCheatSheetDto(cheatSheet);
  }

  @Override
  public CheatSheetDto createCheatSheet(CheatSheetRequestDto cheatSheetRequestDto) {
    // Implementation here
    return null;
  }

  @Override
  public CheatSheetDto updateCheatSheet(UUID id, CheatSheetRequestDto cheatSheetRequestDto) {
    // Implementation here
    return null;
  }

  @Override
  public void deleteCheatSheet(UUID id) {
    // Implementation here
  }
}
