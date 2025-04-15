package com.project.prepnester.service.impl;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.model.SortBy;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.model.userDetails.PrepNesterUserDeatils;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.service.QuestionService;
import com.project.prepnester.service.mapper.QuestionToQuestionDtoMapper;
import com.project.prepnester.service.mapper.SubQuestionMapper;
import com.project.prepnester.util.exceptions.UserDetailsNotFoundException;
import com.project.prepnester.util.jwt.JwtTokenProvider;
import java.util.List;
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

  private final JwtTokenProvider jwtTokenProvider;

  private final UserRepository userRepository;

  @Override
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
  public QuestionDto createQuestion(CreateQuestionBodyRequest body) {
    log.info("Creating question from body: {}", body);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalArgumentException("User not authenticated");
    }

    String email = ((User) authentication.getPrincipal()).getUsername();

    PrepNesterUserDeatils user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new UserDetailsNotFoundException("User with email " + email + " not found"));

    Category category = categoryRepository.findByTitle(
            body.getCategory().getTitle())
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));

    Question question = Question.builder()
        .title(body.getTitle())
        .isPublic(body.getIsPublic())
        .category(category)
        .createdBy(user.getId())
        .updatedBy(user.getId())
        .build();

    question = questionRepository.save(question);

    if (body.getSubQuestions() != null && !body.getSubQuestions().isEmpty()) {
      Question finalQuestion = question;
      List<SubQuestion> subQuestions = body.getSubQuestions().stream()
          .map(SubQuestionMapper.INSTANCE::subQuestionDtoToSubQuestion)
          .peek(sub -> sub.setParentQuestion(finalQuestion))
          .peek(sub -> sub.setCreatedBy(user.getId()))
          .peek(sub -> sub.setUpdatedBy(user.getId()))
          .toList();

      subQuestionRepository.saveAll(subQuestions);
      question.setSubQuestions(subQuestions);
    }

    return QuestionToQuestionDtoMapper.INSTANCE.questionToQuestionDto(
        question);
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
}
