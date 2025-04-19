package com.project.prepnester.controller;

import com.project.prepnester.dto.response.CheatSheetDto;
import com.project.prepnester.service.CheatSheetService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/cheatSheets")
public class CheatSheetController {

  private final CheatSheetService cheatSheetService;

  @GetMapping
  public List<CheatSheetDto> getCheatSheets() {
    return cheatSheetService.getCheatSheets();
  }

  @GetMapping("/{id}")
  public CheatSheetDto getCheatSheetById(@PathVariable UUID id) {
    return cheatSheetService.getCheatSheetById(id);
  }
}
