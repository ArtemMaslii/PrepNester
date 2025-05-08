package com.project.prepnester.services;

import static com.project.prepnester.util.SortingConverter.getSort;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.prepnester.dto.request.CheatSheetRequestDto;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.model.content.CheatSheet;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.repository.CheatSheetRepository;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.service.CheatSheetService;
import com.project.prepnester.service.UserIdService;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CheatSheetServiceTest {

  @Mock
  private CheatSheetRepository cheatSheetRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private LikeRepository likeRepository;

  @Mock
  private UserIdService userIdService;

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private CheatSheetService cheatSheetService;

  private UUID userId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
  }

  @Test
  void getCheatSheets_shouldReturnListOfCheatSheetPreviews() {
    // given
    UUID cheatSheetId = UUID.randomUUID();
    CheatSheet cheatSheet = CheatSheet.builder()
        .id(cheatSheetId)
        .title("Sample CheatSheet")
        .isPublic(true)
        .questions(Collections.emptyList())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .createdBy(userId)
        .updatedBy(userId)
        .build();

    Pageable pageable = PageRequest.of(
        0,
        10,
        getSort(SortBy.ASCENDING)
    );

    when(cheatSheetRepository.findAllByIsPublic(true, pageable)).thenReturn(List.of(cheatSheet));
    when(likeRepository.findAllByCheatSheetId(cheatSheetId)).thenReturn(Collections.emptyList());

    // when
    var result = cheatSheetService.getCheatSheets(new PageInfoDto(0, 10), SortBy.ASCENDING, true,
        null);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(cheatSheetId);
    verify(cheatSheetRepository).findAllByIsPublic(true, pageable);
  }

  @Test
  void getCheatSheetById_shouldReturnCheatSheetDto_whenFound() {
    // given
    UUID cheatSheetId = UUID.randomUUID();
    CheatSheet cheatSheet = CheatSheet.builder()
        .id(cheatSheetId)
        .title("Sample CheatSheet")
        .isPublic(true)
        .categories(Collections.emptyList())
        .questions(Collections.emptyList())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .createdBy(userId)
        .updatedBy(userId)
        .build();

    when(cheatSheetRepository.findById(cheatSheetId)).thenReturn(Optional.of(cheatSheet));

    // when
    var result = cheatSheetService.getCheatSheetById(cheatSheetId);

    // then
    assertThat(result.getId()).isEqualTo(cheatSheetId);
    verify(cheatSheetRepository).findById(cheatSheetId);
  }

  @Test
  void getCheatSheetById_shouldThrowNotFound_whenCheatSheetNotFound() {
    // given
    UUID id = UUID.randomUUID();
    when(cheatSheetRepository.findById(id)).thenReturn(Optional.empty());

    // when / then
    assertThrows(NotFoundException.class, () -> cheatSheetService.getCheatSheetById(id));
  }

  @Test
  void createCheatSheet_shouldSaveAndReturnCheatSheetDto() {
    // given
    CheatSheetRequestDto requestDto = CheatSheetRequestDto.builder()
        .title("New CheatSheet")
        .isPublic(true)
        .categories(Collections.emptyList())
        .createdBy(userId)
        .build();

    UUID savedId = UUID.randomUUID();
    CheatSheet savedCheatSheet = CheatSheet.builder()
        .id(savedId)
        .title("New CheatSheet")
        .isPublic(true)
        .categories(Collections.emptyList())
        .questions(Collections.emptyList())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .createdBy(userId)
        .build();
    
    when(cheatSheetRepository.save(any(CheatSheet.class))).thenReturn(savedCheatSheet);

    // when
    var result = cheatSheetService.createCheatSheet(requestDto);

    // then
    assertThat(result.getTitle()).isEqualTo("New CheatSheet");
    verify(cheatSheetRepository).save(any(CheatSheet.class));
  }

  @Test
  void updateCheatSheet_shouldUpdateAndReturnCheatSheetDto_whenUserHasPermission() {
    // given
    UUID cheatSheetId = UUID.randomUUID();
    CheatSheet existing = CheatSheet.builder()
        .id(cheatSheetId)
        .title("Old Title")
        .isPublic(false)
        .createdBy(userId)
        .categories(Collections.emptyList())
        .questions(Collections.emptyList())
        .build();

    CheatSheetRequestDto updateRequest = CheatSheetRequestDto.builder()
        .title("Updated Title")
        .isPublic(true)
        .categories(Collections.emptyList())
        .build();

    when(cheatSheetRepository.findById(cheatSheetId)).thenReturn(Optional.of(existing));
    when(userIdService.getCurrentUserId()).thenReturn(userId);
    when(cheatSheetRepository.save(any(CheatSheet.class))).thenReturn(existing);

    // when
    var result = cheatSheetService.updateCheatSheet(cheatSheetId, updateRequest);

    // then
    assertThat(result.getTitle()).isEqualTo("Updated Title");
    verify(cheatSheetRepository).save(existing);
  }

  @Test
  void updateCheatSheet_shouldThrowNoPermission_whenUserNotOwner() {
    // given
    UUID cheatSheetId = UUID.randomUUID();
    CheatSheet existing = CheatSheet.builder()
        .id(cheatSheetId)
        .createdBy(userId)
        .build();

    CheatSheetRequestDto updateRequest = CheatSheetRequestDto.builder()
        .title("Updated Title")
        .isPublic(true)
        .categories(Collections.emptyList())
        .build();

    when(cheatSheetRepository.findById(cheatSheetId)).thenReturn(Optional.of(existing));
    when(userIdService.getCurrentUserId()).thenReturn(UUID.randomUUID());

    // when / then
    assertThrows(NoPermissionException.class,
        () -> cheatSheetService.updateCheatSheet(cheatSheetId, updateRequest));
  }

  @Test
  void deleteCheatSheet_shouldDelete_whenUserIsOwner() {
    // given
    UUID cheatSheetId = UUID.randomUUID();
    CheatSheet cheatSheet = CheatSheet.builder()
        .id(cheatSheetId)
        .createdBy(userId)
        .build();

    when(cheatSheetRepository.findById(cheatSheetId)).thenReturn(Optional.of(cheatSheet));
    when(userIdService.getCurrentUserId()).thenReturn(userId);

    // when
    cheatSheetService.deleteCheatSheet(cheatSheetId);

    // then
    verify(cheatSheetRepository).delete(cheatSheet);
  }

  @Test
  void deleteCheatSheet_shouldThrowNoPermission_whenUserIsNotOwner() {
    // given
    UUID cheatSheetId = UUID.randomUUID();
    CheatSheet cheatSheet = CheatSheet.builder()
        .id(cheatSheetId)
        .createdBy(UUID.randomUUID())
        .build();

    when(cheatSheetRepository.findById(cheatSheetId)).thenReturn(Optional.of(cheatSheet));
    when(userIdService.getCurrentUserId()).thenReturn(userId);

    // when / then
    assertThrows(NoPermissionException.class,
        () -> cheatSheetService.deleteCheatSheet(cheatSheetId));
  }
}
