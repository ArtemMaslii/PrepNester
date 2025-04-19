package com.project.prepnester.service.impl;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.SubQuestionDtoRequest;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.QuestionService;
import com.project.prepnester.service.mapper.QuestionToQuestionDtoMapper;
import com.project.prepnester.service.mapper.SubQuestionMapper;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;

  private final SubQuestionRepository subQuestionRepository;

  private final CategoryRepository categoryRepository;

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public List<QuestionDto> getAllQuestions(PageInfoDto pageInfoDto, SortBy sortBy,
      Boolean isPublic) {
    Pageable pageable = PageRequest.of(
        pageInfoDto.getPage(),
        pageInfoDto.getSize(),
        getSort(sortBy)
    );

    log.info("Fetching questions with page: {}, size: {}, isPublic: {}, sortBy: {}",
        pageInfoDto.getPage(),
        pageInfoDto.getSize(),
        isPublic,
        sortBy);

    List<Question> fetchedQuestions = questionRepository.findAllByIsPublic(isPublic, pageable);

    return fetchedQuestions.stream()
        .map(QuestionToQuestionDtoMapper.INSTANCE::questionToQuestionDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public QuestionDto getQuestionById(UUID questionId) {
    log.info("Fetching question with id: {}", questionId);

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new IllegalArgumentException("Question not found"));

    return QuestionToQuestionDtoMapper.INSTANCE.questionToQuestionDto(question);
  }

  @Override
  @Transactional(readOnly = true)
  public SubQuestionDto getSubQuestionById(UUID subQuestionId) {
    log.info("Fetching sub-questions for question with id: {}", subQuestionId);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new IllegalArgumentException("Sub question not found"));

    return SubQuestionMapper.INSTANCE.subQuestionToSubQuestionDto(subQuestion);
  }

  @Override
  public QuestionDto createQuestion(CreateQuestionBodyRequest body) {
    log.info("Creating question from body: {}", body);

    Category category = categoryRepository.findByTitle(
            body.getCategory().getTitle())
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));

    Question question = Question.builder()
        .title(body.getTitle())
        .isPublic(body.getIsPublic())
        .category(category)
        .createdBy(body.getCreatedBy())
        .updatedBy(body.getCreatedBy())
        .createdAt(LocalDateTime.now())
        .build();

    question = questionRepository.save(question);

    if (body.getSubQuestions() != null && !body.getSubQuestions().isEmpty()) {
      Question finalQuestion = question;
      List<SubQuestion> subQuestions = body.getSubQuestions().stream()
          .map(SubQuestionMapper.INSTANCE::subQuestionDtoToSubQuestion)
          .peek(sub -> sub.setParentQuestion(finalQuestion))
          .peek(sub -> sub.setCreatedBy(body.getCreatedBy()))
          .peek(sub -> sub.setUpdatedBy(body.getCreatedBy()))
          .peek(sub -> sub.setCreatedAt(finalQuestion.getCreatedAt()))
          .toList();

      subQuestionRepository.saveAll(subQuestions);
      question.setSubQuestions(subQuestions);
    }

    return QuestionToQuestionDtoMapper.INSTANCE.questionToQuestionDto(
        question);
  }

  @Override
  public QuestionDto updateQuestion(UUID questionId, CreateQuestionBodyRequest body) {
    log.info("Updating question with id: {} from body: {}", questionId, body);

    if (!getCurrentUserId().equals(body.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new IllegalArgumentException("Question not found"));

    question.setTitle(body.getTitle());
    question.setIsPublic(body.getIsPublic());
    question.setUpdatedAt(LocalDateTime.now());

    if (body.getSubQuestions() != null) {
      Map<UUID, SubQuestion> existingMap = question.getSubQuestions().stream()
          .collect(Collectors.toMap(SubQuestion::getId, sub -> sub));

      for (SubQuestionDto subDto : body.getSubQuestions()) {
        if (subDto.getId() != null && existingMap.containsKey(subDto.getId())) {
          SubQuestion existing = existingMap.get(subDto.getId());
          existing.setTitle(subDto.getTitle());
        }
      }

      subQuestionRepository.saveAll(existingMap.values());
    }

    return QuestionToQuestionDtoMapper.INSTANCE.questionToQuestionDto(
        questionRepository.save(question)
    );
  }

  @Override
  public SubQuestionDto updateSubQuestion(UUID subQuestionId, SubQuestionDtoRequest request) {
    log.info("Updating question with id: {} and title: {}", subQuestionId, request);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new IllegalArgumentException("Sub-question not found"));

    subQuestion.setTitle(request.getTitle());
    subQuestion.setUpdatedAt(LocalDateTime.now());

    subQuestion = subQuestionRepository.save(subQuestion);
    return SubQuestionMapper.INSTANCE.subQuestionToSubQuestionDto(subQuestion);
  }

  @Override
  public void deleteQuestion(UUID questionId) {
    log.info("Deleting question with id: {}", questionId);

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new IllegalArgumentException("Question not found"));

    if (!getCurrentUserId().equals(question.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    questionRepository.delete(question);
  }

  @Override
  public void deleteSubQuestion(UUID subQuestionId) {
    log.info("Deleting sub-question with id: {}", subQuestionId);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new IllegalArgumentException("Sub-question not found"));

    if (!getCurrentUserId().equals(subQuestion.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    subQuestionRepository.delete(subQuestion);
  }

  private Sort getSort(SortBy sortBy) {
    return switch (sortBy) {
      case ASCENDING -> Sort.by(Sort.Direction.ASC, "title");
      case DESCENDING -> Sort.by(Sort.Direction.DESC, "title");
      case RECENTLY_CREATED -> Sort.by(Sort.Direction.DESC, "createdAt");
      case RECENTLY_UPDATED -> Sort.by(Sort.Direction.DESC, "updatedAt");
      case MOST_LIKED -> Sort.unsorted();
      case MOST_COMMENTED -> Sort.unsorted();
    };
  }

  private UUID getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalArgumentException("User not authenticated");
    }

    String email = ((User) authentication.getPrincipal()).getUsername();

    PrepNesterUserDetails user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException("User with email " + email + " not found"));

    return user.getId();
  }
}
