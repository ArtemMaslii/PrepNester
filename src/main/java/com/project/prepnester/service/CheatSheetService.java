package com.project.prepnester.service;

import static com.project.prepnester.util.SortingConverter.getSort;

import com.project.prepnester.dto.request.CategoryWithQuestionsRequestDto;
import com.project.prepnester.dto.request.CheatSheetRequestDto;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.QuestionIdsRequestDto;
import com.project.prepnester.dto.response.CategoryWithQuestionsDto;
import com.project.prepnester.dto.response.CheatSheetDto;
import com.project.prepnester.dto.response.CheatSheetPreview;
import com.project.prepnester.dto.response.QuestionWithoutCategoryDto;
import com.project.prepnester.model.common.SortBy;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.model.content.CheatSheet;
import com.project.prepnester.model.content.Question;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.repository.CheatSheetRepository;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.LikeRepository;
import com.project.prepnester.repository.QuestionRepository;
import com.project.prepnester.service.mapper.QuestionMapper;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CheatSheetService {

  private final CheatSheetRepository cheatSheetRepository;

  private final QuestionRepository questionRepository;

  private final CategoryRepository categoryRepository;

  private final LikeRepository likeRepository;

  private final UserIdService userIdService;

  private final CommentRepository commentRepository;

  public List<CheatSheetPreview> getCheatSheets(PageInfoDto pageInfoDto, SortBy sortBy,
      Boolean isPublic, String search) {
    Pageable pageable = PageRequest.of(
        pageInfoDto.getPage(),
        pageInfoDto.getSize(),
        getSort(sortBy)
    );

    log.info("Fetching all cheat sheets from the database");

    List<CheatSheet> cheatSheets = search == null || search.isEmpty()
        ? cheatSheetRepository.findAllByIsPublic(isPublic, pageable)
        : cheatSheetRepository.findAllByIsPublicAndTitleContainingIgnoreCase(isPublic, search);

    List<CheatSheetPreview> cheatSheetPreviews = cheatSheets
        .stream()
        .map(cheatSheet -> CheatSheetPreview.builder()
            .id(cheatSheet.getId())
            .title(cheatSheet.getTitle())
            .isPublic(cheatSheet.getIsPublic())
            .likesCount((long) likeRepository.findAllByCheatSheetId(cheatSheet.getId()).size())
            .commentsCount(
                cheatSheet.getQuestions().stream()
                    .mapToLong(question -> commentRepository.findAllByQuestionId(question.getId())
                        .size())
                    .sum()
            )
            .createdAt(cheatSheet.getCreatedAt())
            .updatedAt(cheatSheet.getUpdatedAt())
            .createdBy(cheatSheet.getCreatedBy())
            .updatedBy(cheatSheet.getUpdatedBy())
            .isLikedByCurrentUser(
                likeRepository.existsByCheatSheetIdAndUserId(cheatSheet.getId(),
                    userIdService.getCurrentUserId()))
            .build())
        .toList();

    Comparator<CheatSheetPreview> byTotalLikes = Comparator.comparingLong(
        CheatSheetPreview::getLikesCount);

    Comparator<CheatSheetPreview> byTotalComments = Comparator.comparingLong(
        CheatSheetPreview::getCommentsCount);

    if (sortBy == SortBy.MOST_LIKED) {
      cheatSheetPreviews = cheatSheetPreviews.stream()
          .sorted(byTotalLikes.reversed())
          .toList();
    } else if (sortBy == SortBy.MOST_COMMENTED) {
      cheatSheetPreviews = cheatSheetPreviews.stream()
          .sorted(byTotalComments.reversed())
          .toList();
    }

    return cheatSheetPreviews;
  }

  public CheatSheetDto getCheatSheetById(UUID id) {
    log.info("Fetching cheat sheet with id: {}", id);

    CheatSheet cheatSheet = cheatSheetRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cheat sheet not found"));

    return CheatSheetDto.builder()
        .id(cheatSheet.getId())
        .title(cheatSheet.getTitle())
        .isPublic(cheatSheet.getIsPublic())
        .categories(getCheatSheetsCategories(cheatSheet))
        .createdAt(cheatSheet.getCreatedAt())
        .updatedAt(cheatSheet.getUpdatedAt())
        .createdBy(cheatSheet.getCreatedBy())
        .updatedBy(cheatSheet.getUpdatedBy())
        .build();
  }

  public CheatSheetPreview createCheatSheet(CheatSheetRequestDto body) {
    log.info("Creating a new cheat sheet from body: {}", body);

    Pair<List<Category>, List<Question>> data = extractCategoriesAndQuestions(body);

    CheatSheet cheatSheet = CheatSheet.builder()
        .title(body.getTitle())
        .categories(data.getLeft())
        .questions(data.getRight())
        .isPublic(body.getIsPublic())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .createdBy(userIdService.getCurrentUserId())
        .updatedBy(null)
        .build();

    CheatSheet saved = cheatSheetRepository.save(cheatSheet);

    return CheatSheetPreview.builder()
        .id(saved.getId())
        .title(saved.getTitle())
        .isPublic(saved.getIsPublic())
        .createdAt(saved.getCreatedAt())
        .updatedAt(saved.getUpdatedAt())
        .createdBy(saved.getCreatedBy())
        .updatedBy(saved.getUpdatedBy())
        .likesCount((long) likeRepository.findAllByCheatSheetId(cheatSheet.getId()).size())
        .commentsCount(saved.getQuestions().stream()
            .mapToLong(question -> commentRepository.findAllByQuestionId(question.getId())
                .size())
            .sum())
        .isLikedByCurrentUser(false)
        .build();
  }

  public CheatSheetDto updateCheatSheet(UUID id, CheatSheetRequestDto body) {
    log.info("Updating a cheat sheet with id: {}, from body: {}", id, body);

    CheatSheet cheatSheet = cheatSheetRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cheat sheet not found"));

    if (!userIdService.getCurrentUserId().equals(cheatSheet.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    Pair<List<Category>, List<Question>> data = extractCategoriesAndQuestions(body);

    cheatSheet.setTitle(body.getTitle());
    cheatSheet.setCategories(data.getLeft());
    cheatSheet.setQuestions(data.getRight());
    cheatSheet.setIsPublic(body.getIsPublic());
    cheatSheet.setUpdatedAt(LocalDateTime.now());
    cheatSheet.setUpdatedBy(userIdService.getCurrentUserId());

    CheatSheet updated = cheatSheetRepository.save(cheatSheet);

    return CheatSheetDto.builder()
        .id(cheatSheet.getId())
        .title(cheatSheet.getTitle())
        .isPublic(cheatSheet.getIsPublic())
        .categories(
            getCheatSheetsCategories(updated))
        .createdAt(cheatSheet.getCreatedAt())
        .updatedAt(cheatSheet.getUpdatedAt())
        .createdBy(cheatSheet.getCreatedBy())
        .updatedBy(cheatSheet.getUpdatedBy())
        .build();
  }

  public void deleteCheatSheet(UUID id) {
    log.info("Deleting cheat sheet with id: {}", id);

    CheatSheet cheatSheet = cheatSheetRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cheat sheet not found"));

    if (!userIdService.getCurrentUserId().equals(cheatSheet.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    cheatSheetRepository.delete(cheatSheet);

    log.info("Cheat sheet with id: {} has been deleted successfully", id);
  }

  public List<CategoryWithQuestionsDto> getCheatSheetsCategories(CheatSheet cheatSheet) {

    return cheatSheet.getCategories().stream()
        .map(category -> {
          List<QuestionWithoutCategoryDto> filteredQuestions = cheatSheet.getQuestions()
              .stream()
              .filter(q -> q.getCategory().getTitle().equals(category.getTitle()))
              .map(question ->
                  QuestionWithoutCategoryDto.builder()
                      .id(question.getId())
                      .title(question.getTitle())
                      .isPublic(question.getIsPublic())
                      .likesCount(
                          (long) likeRepository.findAllByQuestionId(question.getId()).size())
                      .commentsCount(
                          (long) commentRepository.findAllByQuestionId(question.getId()).size()
                      )
                      .subQuestions(question.getSubQuestions().stream()
                          .map(QuestionMapper::mapSubQuestionToDtoWithoutComments)
                          .toList())
                      .createdAt(question.getCreatedAt())
                      .updatedAt(question.getUpdatedAt())
                      .build()
              )
              .toList();

          return CategoryWithQuestionsDto.builder()
              .id(category.getId())
              .title(category.getTitle())
              .questions(filteredQuestions)
              .build();
        }).toList();
  }

  private Pair<List<Category>, List<Question>> extractCategoriesAndQuestions(
      CheatSheetRequestDto body) {
    List<UUID> categoryIds = body.getCategories().stream()
        .map(CategoryWithQuestionsRequestDto::getId)
        .toList();

    List<UUID> questionIds = body.getCategories().stream()
        .flatMap(cat -> cat.getQuestions().stream().map(QuestionIdsRequestDto::getId))
        .toList();

    List<Category> categories = categoryRepository.findAllById(categoryIds);
    List<Question> questions = questionRepository.findAllById(questionIds);

    return Pair.of(categories, questions);
  }


}
