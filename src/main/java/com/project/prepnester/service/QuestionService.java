package com.project.prepnester.service;

import static com.project.prepnester.service.mapper.QuestionMapper.mapQuestionDetailsToDto;
import static com.project.prepnester.service.mapper.QuestionMapper.mapQuestionToDto;
import static com.project.prepnester.util.SortingConverter.getSort;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.UpdateQuestionBodyRequest;
import com.project.prepnester.dto.response.QuestionDetailsDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.dto.response.SubQuestionWithoutCommentsDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.model.content.Comment;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
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
import java.util.Map;
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
        if (sub instanceof SubQuestionWithoutCommentsDto subDto) {
          totalLikes += subDto.getLikesCount();
        }
      }
      return totalLikes;
    });

    Comparator<QuestionDto> byTotalComments = Comparator.comparingLong(dto -> {
      Long totalComments = dto.getCommentsCount();
      for (Object sub : dto.getSubQuestions()) {
        if (sub instanceof SubQuestionWithoutCommentsDto subDto) {
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

    List<Comment> comments = commentRepository.findAllByQuestionId(questionId);

    Map<UUID, String> userNamesByIdCreated = comments.stream()
        .collect(Collectors.toMap(
            Comment::getCreatedBy,
            comment -> userRepository.findById(comment.getCreatedBy())
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getFullName(),
            (existing, replacement) -> existing
        ));

    Map<UUID, String> userNamesByIdUpdated = comments.stream()
        .filter(comment -> comment.getUpdatedBy() != null)
        .collect(Collectors.toMap(
            Comment::getUpdatedBy,
            comment -> userRepository.findById(comment.getUpdatedBy())
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getFullName(),
            (existing, replacement) -> existing
        ));

    Map<UUID, Boolean> likesCommentIds = comments.stream()
        .collect(Collectors.toMap(Comment::getId,
            comment -> likeRepository.existsByCommentIdAndUserId(comment.getId(),
                userIdService.getCurrentUserId())));

    return mapQuestionDetailsToDto(question, createdBy, updatedBy,
        commentRepository.findAllByQuestionId(questionId), userNamesByIdCreated,
        userNamesByIdUpdated,
        likeRepository.findAllByQuestionId(
            questionId),
        likesCommentIds);
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

  public QuestionDetailsDto updateQuestion(UUID questionId, UpdateQuestionBodyRequest body) {
    log.info("Updating question with id: {} from body: {}", questionId, body);

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

    question.setTitle(body.getTitle());
    question.setUpdatedAt(LocalDateTime.now());

    List<Comment> comments = commentRepository.findAllByQuestionId(questionId);

    Map<UUID, String> userNamesByIdCreated = comments.stream()
        .collect(Collectors.toMap(
            Comment::getCreatedBy,
            comment -> {
              PrepNesterUserDetails user = userRepository.findById(comment.getCreatedBy())
                  .orElseThrow(() -> new NotFoundException("User not found"));
              return user.getFullName();
            },
            (existing, replacement) -> existing // handle duplicate keys
        ));

    Map<UUID, String> userNamesByIdUpdated = comments.stream()
        .collect(Collectors.toMap(
            Comment::getUpdatedBy,
            comment -> {
              PrepNesterUserDetails user = userRepository.findById(comment.getCreatedBy())
                  .orElseThrow(() -> new NotFoundException("User not found"));
              return user.getFullName();
            },
            (existing, replacement) -> existing // handle duplicate keys
        ));

    Map<UUID, Boolean> likesCommentIds = comments.stream()
        .collect(Collectors.toMap(Comment::getId,
            comment -> likeRepository.existsByCommentIdAndUserId(comment.getId(),
                userIdService.getCurrentUserId())));

    return mapQuestionDetailsToDto(question, createdBy, updatedBy,
        commentRepository.findAllByQuestionId(questionId), userNamesByIdCreated,
        userNamesByIdUpdated,
        likeRepository.findAllByQuestionId(
            questionId),
        likesCommentIds);
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
