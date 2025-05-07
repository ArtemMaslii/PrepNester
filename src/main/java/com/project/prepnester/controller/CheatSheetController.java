package com.project.prepnester.controller;

import com.project.prepnester.dto.request.CheatSheetRequestDto;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.UserIdDto;
import com.project.prepnester.dto.response.CheatSheetDto;
import com.project.prepnester.dto.response.CheatSheetPreview;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.service.CheatSheetService;
import com.project.prepnester.service.LikeService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/cheatSheets")
public class CheatSheetController {

  private final CheatSheetService cheatSheetService;

  private final LikeService likeService;

  @GetMapping
  public List<CheatSheetPreview> getCheatSheets(
      @RequestParam(required = false, defaultValue = "true") Boolean isPublic,
      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
      @RequestParam(required = false, defaultValue = "25") Integer pageSize,
      @RequestParam(required = false, defaultValue = "ASCENDING") SortBy sortBy,
      @RequestParam(required = false) String search) {
    return cheatSheetService.getCheatSheets(new PageInfoDto(pageNum, pageSize), sortBy, isPublic,
        search);
  }

  @GetMapping("/{id}")
  public CheatSheetDto getCheatSheetById(@PathVariable UUID id) {
    return cheatSheetService.getCheatSheetById(id);
  }

  @PostMapping
  public ResponseEntity<CheatSheetDto> createCheatSheet(
      @RequestBody CheatSheetRequestDto cheatSheetRequestDto) {
    CheatSheetDto cheatSheetDto = cheatSheetService.createCheatSheet(cheatSheetRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(cheatSheetDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CheatSheetDto> updateCheatSheet(
      @PathVariable UUID id,
      @RequestBody CheatSheetRequestDto cheatSheetRequestDto) {
    CheatSheetDto cheatSheetDto = cheatSheetService.updateCheatSheet(id, cheatSheetRequestDto);
    return ResponseEntity.ok(cheatSheetDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCheatSheet(@PathVariable UUID id) {
    cheatSheetService.deleteCheatSheet(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/like")
  public ResponseEntity<Void> likeCheatSheet(@PathVariable UUID id, @RequestBody UserIdDto user) {
    likeService.likeCheatSheet(id, user.getUserId());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}/like")
  public ResponseEntity<Void> removeLikeCheatSheet(@PathVariable UUID id,
      @RequestParam UUID userId) {
    likeService.removeCheatSheetLike(id, userId);
    return ResponseEntity.ok().build();
  }
}
