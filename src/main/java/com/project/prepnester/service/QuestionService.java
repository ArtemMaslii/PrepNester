package com.project.prepnester.service;

import static com.project.prepnester.service.mapper.QuestionMapper.mapQuestionDetailsToDto;
import static com.project.prepnester.service.mapper.QuestionMapper.mapQuestionToDto;
import static com.project.prepnester.util.SortingConverter.getSort;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.UpdateQuestionBodyRequest;
import com.project.prepnester.dto.response.QuestionDetailsDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class QuestionService {

  private final QuestionRepository questionRepository;

  private final SubQuestionRepository subQuestionRepository;

  private final CategoryRepository categoryRepository;

  private final UserIdService userIdService;

  private final UserRepository userRepository;

  private final CommentRepository commentRepository;

  private final LikeRepository likeRepository;


  @Transactional(readOnly = true)
  public List<QuestionDto> getAllQuestions(PageInfoDto pageInfoDto, SortBy sortBy,
      Boolean isPublic, String search) {
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

    List<Question> questions;

    if (search == null || search.isBlank()) {
      questions = questionRepository.findAllByIsPublic(isPublic, pageable);
    } else {
      questions = questionRepository.findAllByIsPublicAndTitleOrSubTitleContains(
          isPublic,
          search.toLowerCase()
      );
    }

    List<UUID> likedSubQuestionIds = questions.stream()
        .flatMap(q -> q.getSubQuestions().stream())
        .map(SubQuestion::getId).filter(id -> likeRepository.existsBySubQuestionIdAndUserId(id,
            userIdService.getCurrentUserId())).toList();

    List<QuestionDto> questionDtos = questions.stream()
        .map(question -> mapQuestionToDto(question,
            commentRepository.findAllByQuestionId(question.getId()),
            likeRepository.findAllByQuestionId(question.getId()),
            likeRepository.existsByQuestionIdAndUserId(
                question.getId(),
                userIdService.getCurrentUserId()
            ),
            likedSubQuestionIds))
        .toList();

    Comparator<QuestionDto> byTotalLikes = Comparator.comparingLong(dto -> {
      Long totalLikes = dto.getLikesCount();
      for (Object sub : dto.getSubQuestions()) {
        if (sub instanceof QuestionDto subDto) {
          totalLikes += subDto.getLikesCount();
        }
      }
      return totalLikes;
    });

    Comparator<QuestionDto> byTotalComments = Comparator.comparingLong(dto -> {
      Long totalComments = dto.getCommentsCount();
      for (Object sub : dto.getSubQuestions()) {
        if (sub instanceof QuestionDto subDto) {
          totalComments += subDto.getCommentsCount();
        }
      }
      return totalComments;
    });

    if (sortBy == SortBy.MOST_LIKED) {
      questionDtos = questionDtos.stream()
          .sorted(byTotalLikes.reversed())
          .toList();
    } else if (sortBy == SortBy.MOST_COMMENTED) {
      questionDtos = questionDtos.stream()
          .sorted(byTotalComments.reversed())
          .toList();
    }

    return questionDtos;
  }


  @Transactional(readOnly = true)
  public QuestionDetailsDto getQuestionById(UUID questionId) {
    log.info("Fetching question with id: {}", questionId);

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new NotFoundException("Question not found"));

    String createdBy = userRepository.findById(question.getCreatedBy())
        .orElseThrow(
            () -> new NotFoundException("User with id " + question.getCreatedBy() + " not found"))
        .getFullName();

    String updatedBy = null;

    if (question.getUpdatedBy() != null) {
      updatedBy = userRepository.findById(question.getUpdatedBy())
          .orElseThrow(
              () -> new NotFoundException(
                  "User with id " + question.getUpdatedBy() + " not found"))
          .getFullName();
    }

    return mapQuestionDetailsToDto(question, createdBy, updatedBy,
        commentRepository.findAllByQuestionId(questionId));
  }


  @Transactional
  public QuestionDto createQuestion(CreateQuestionBodyRequest body) {
    log.info("Creating question from body: {}", body);

    Category category = categoryRepository.findByTitle(
            body.getCategory().getTitle())
        .orElseThrow(() -> new NotFoundException("Category not found"));

    Question question = Question.builder()
        .title(body.getTitle())
        .isPublic(body.getIsPublic())
        .category(category)
        .createdBy(body.getCreatedBy())
        .createdAt(LocalDateTime.now())
        .build();

    Question saved = questionRepository.save(question);
    questionRepository.flush();

    List<SubQuestion> subQuestions = List.of();
    if (body.getSubQuestions() != null && !body.getSubQuestions().isEmpty()) {
      subQuestions = body.getSubQuestions().stream()
          .map(sub -> SubQuestion.builder()
              .title(sub.getTitle())
              .parentQuestion(saved)
              .createdBy(body.getCreatedBy())
              .createdAt(LocalDateTime.now())
              .comments(List.of())
              .likes(List.of())
              .build())
          .collect(Collectors.toList());

      subQuestionRepository.saveAll(subQuestions);
    }

    saved.setSubQuestions(subQuestions);
    return mapQuestionToDto(saved, List.of(), List.of(), false, List.of());
  }

  public QuestionDto updateQuestion(UUID questionId, UpdateQuestionBodyRequest body) {
    log.info("Updating question with id: {} from body: {}", questionId, body);

    if (!userIdService.getCurrentUserId().equals(body.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new NotFoundException("Question not found"));

    question.setTitle(body.getTitle());
    question.setIsPublic(body.getIsPublic());
    question.setUpdatedAt(LocalDateTime.now());

    List<UUID> likedSubQuestionIds = question.getSubQuestions().stream()
        .map(SubQuestion::getId).filter(id -> likeRepository.existsBySubQuestionIdAndUserId(id,
            userIdService.getCurrentUserId())).toList();

    return mapQuestionToDto(questionRepository.save(question),
        commentRepository.findAllByQuestionId(questionId),
        likeRepository.findAllByQuestionId(questionId),
        likeRepository.existsByQuestionIdAndUserId(
            questionId,
            userIdService.getCurrentUserId()
        ), likedSubQuestionIds);
  }


  public void deleteQuestion(UUID questionId) {
    log.info("Deleting question with id: {}", questionId);

    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new IllegalArgumentException("Question not found"));

    if (!userIdService.getCurrentUserId().equals(question.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }
    questionRepository.delete(question);
  }
}
