package com.project.prepnester.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.UpdateQuestionBodyRequest;
import com.project.prepnester.dto.response.CategoryDto;
import com.project.prepnester.dto.response.QuestionDetailsDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.QuestionService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private SubQuestionRepository subQuestionRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private UserIdService userIdService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private LikeRepository likeRepository;

  @InjectMocks
  private QuestionService questionService;

  private UUID questionId;
  private UUID userId;

  @BeforeEach
  void setUp() {
    questionId = UUID.randomUUID();
    userId = UUID.randomUUID();
  }

  @Test
  void testGetAllQuestions_happyPath() {
    PageInfoDto pageInfo = new PageInfoDto(0, 10);
    Question question = Question.builder()
        .id(questionId)
        .title("Title")
        .subQuestions(List.of())
        .isPublic(true)
        .createdBy(userId)
        .createdAt(LocalDateTime.now())
        .build();

    when(questionRepository.findAllByIsPublic(true,
        PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"))))
        .thenReturn(List.of(question));
    when(commentRepository.findAllByQuestionId(questionId)).thenReturn(List.of());
    when(likeRepository.findAllByQuestionId(questionId)).thenReturn(List.of());

    List<QuestionDto> result = questionService.getAllQuestions(pageInfo, SortBy.ASCENDING, true,
        "");

    assertThat(result).hasSize(1);
    verify(questionRepository).findAllByIsPublic(true,
        PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title")));
  }

  @Test
  void testGetAllQuestions_emptyList() {
    PageInfoDto pageInfo = new PageInfoDto(0, 10);

    when(questionRepository.findAllByIsPublic(true,
        PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"))))
        .thenReturn(List.of());

    List<QuestionDto> result = questionService.getAllQuestions(pageInfo, SortBy.ASCENDING, true,
        "");

    assertThat(result).isEmpty();
  }

  @Test
  void testGetQuestionById_happyPath() {
    Question question = Question.builder()
        .id(questionId)
        .subQuestions(List.of())
        .title("Sample")
        .createdBy(userId)
        .build();

    when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    when(userRepository.findById(userId)).thenReturn(
        Optional.of(mockUser()));
    when(commentRepository.findAllByQuestionId(questionId)).thenReturn(List.of());

    QuestionDetailsDto result = questionService.getQuestionById(questionId);

    assertThat(result).isNotNull();
    verify(questionRepository).findById(questionId);
  }

  @Test
  void testGetQuestionById_questionNotFound() {
    when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> questionService.getQuestionById(questionId));
  }

  @Test
  void testGetQuestionById_createdByUserNotFound() {
    Question question = Question.builder()
        .id(questionId)
        .createdBy(userId)
        .build();

    when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> questionService.getQuestionById(questionId));
  }

  @Test
  void testCreateQuestion_happyPath() {
    CreateQuestionBodyRequest body = new CreateQuestionBodyRequest();
    body.setTitle("New Question");
    body.setIsPublic(true);
    body.setCreatedBy(userId);
    CategoryDto categoryDto = CategoryDto.builder()
        .id(UUID.randomUUID())
        .title("Tech")
        .build();
    body.setCategory(categoryDto);

    Category category = Category.builder().id(UUID.randomUUID()).title("Tech").build();
    Question question = Question.builder()
        .id(questionId)
        .title("New Question")
        .isPublic(true)
        .category(category)
        .createdBy(userId)
        .createdAt(LocalDateTime.now())
        .build();

    when(categoryRepository.findByTitle("Tech")).thenReturn(Optional.of(category));
    when(questionRepository.save(any(Question.class))).thenReturn(question);

    QuestionDto result = questionService.createQuestion(body);

    assertThat(result).isNotNull();
    verify(questionRepository).save(any(Question.class));
    verify(questionRepository).flush();
  }

  @Test
  void testCreateQuestion_categoryNotFound() {
    CreateQuestionBodyRequest body = new CreateQuestionBodyRequest();
    CategoryDto categoryDto = CategoryDto.builder()
        .id(UUID.randomUUID())
        .title("Unknown")
        .build();
    body.setCategory(categoryDto);

    when(categoryRepository.findByTitle("Unknown")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> questionService.createQuestion(body));
  }

  @Test
  void testUpdateQuestion_noPermission() {
    UpdateQuestionBodyRequest body = new UpdateQuestionBodyRequest();
    body.setCreatedBy(UUID.randomUUID());

    when(userIdService.getCurrentUserId()).thenReturn(userId);

    assertThrows(NoPermissionException.class,
        () -> questionService.updateQuestion(questionId, body));
  }

  @Test
  void testUpdateQuestion_questionNotFound() {
    UpdateQuestionBodyRequest body = new UpdateQuestionBodyRequest();
    body.setCreatedBy(userId);

    when(userIdService.getCurrentUserId()).thenReturn(userId);
    when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> questionService.updateQuestion(questionId, body));
  }

  @Test
  void testDeleteQuestion_happyPath() {
    Question question = Question.builder()
        .id(questionId)
        .createdBy(userId)
        .build();

    when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    when(userIdService.getCurrentUserId()).thenReturn(userId);

    questionService.deleteQuestion(questionId);

    verify(questionRepository).delete(question);
  }

  @Test
  void testDeleteQuestion_noPermission() {
    Question question = Question.builder()
        .id(questionId)
        .createdBy(UUID.randomUUID())
        .build();

    when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    when(userIdService.getCurrentUserId()).thenReturn(userId);

    assertThrows(NoPermissionException.class, () -> questionService.deleteQuestion(questionId));
  }

  @Test
  void testDeleteQuestion_questionNotFound() {
    when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> questionService.deleteQuestion(questionId));
  }

  private PrepNesterUserDetails mockUser() {
    PrepNesterUserDetails user = new PrepNesterUserDetails();
    user.setId(userId);
    user.setFullName("Sample User");
    return user;
  }
}
