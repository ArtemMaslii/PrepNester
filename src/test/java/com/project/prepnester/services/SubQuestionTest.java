package com.project.prepnester.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.SubQuestionService;
import com.project.prepnester.service.UserIdService;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubQuestionTest {

  @Mock
  private SubQuestionRepository subQuestionRepository;

  @Mock
  private UserIdService userIdService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private LikeRepository likeRepository;

  @InjectMocks
  private SubQuestionService subQuestionService;

  private UUID subQuestionId;
  private SubQuestion subQuestion;

  @BeforeEach
  void setUp() {
    subQuestionId = UUID.randomUUID();
    subQuestion = SubQuestion.builder()
        .id(subQuestionId)
        .title("Test SubQuestion")
        .createdBy(UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void testGetSubQuestionById_HappyPath() {
    UUID createdById = UUID.randomUUID();
    when(subQuestionRepository.findById(subQuestionId)).thenReturn(Optional.of(subQuestion));
    when(userRepository.findById(subQuestion.getCreatedBy())).thenReturn(Optional.of(
        PrepNesterUserDetails.builder().id(subQuestion.getCreatedBy()).fullName("Creator Name")
            .build()
    ));
    when(commentRepository.findAllBySubQuestionId(subQuestionId)).thenReturn(List.of());
    when(likeRepository.findAllBySubQuestionId(subQuestionId)).thenReturn(List.of());

    var result = subQuestionService.getSubQuestionById(subQuestionId);

    assertThat(result).isNotNull();
    assertEquals("Test SubQuestion", result.getTitle());
    verify(subQuestionRepository).findById(subQuestionId);
  }

  @Test
  void testGetSubQuestionById_SubQuestionNotFound() {
    when(subQuestionRepository.findById(subQuestionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> subQuestionService.getSubQuestionById(subQuestionId));
  }

  @Test
  void testGetSubQuestionById_CreatedByUserNotFound() {
    when(subQuestionRepository.findById(subQuestionId)).thenReturn(Optional.of(subQuestion));
    when(userRepository.findById(subQuestion.getCreatedBy())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> subQuestionService.getSubQuestionById(subQuestionId));
  }

  @Test
  void testDeleteSubQuestion_HappyPath() {
    UUID currentUserId = UUID.randomUUID();
    subQuestion.setCreatedBy(currentUserId);
    when(subQuestionRepository.findById(subQuestionId)).thenReturn(Optional.of(subQuestion));
    when(userIdService.getCurrentUserId()).thenReturn(currentUserId);

    subQuestionService.deleteSubQuestion(subQuestionId);

    verify(subQuestionRepository).delete(subQuestion);
  }

  @Test
  void testDeleteSubQuestion_SubQuestionNotFound() {
    when(subQuestionRepository.findById(subQuestionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> subQuestionService.deleteSubQuestion(subQuestionId));
  }

  @Test
  void testDeleteSubQuestion_NoPermission() {
    UUID differentUserId = UUID.randomUUID();
    subQuestion.setCreatedBy(UUID.randomUUID());
    when(subQuestionRepository.findById(subQuestionId)).thenReturn(Optional.of(subQuestion));
    when(userIdService.getCurrentUserId()).thenReturn(differentUserId);

    assertThrows(NoPermissionException.class,
        () -> subQuestionService.deleteSubQuestion(subQuestionId));
  }
}
