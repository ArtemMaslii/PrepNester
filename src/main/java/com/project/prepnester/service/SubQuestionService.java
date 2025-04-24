package com.project.prepnester.service;

import static com.project.prepnester.service.mapper.QuestionMapper.mapSubQuestionToDto;
import static com.project.prepnester.service.mapper.QuestionMapper.mapSubQuestionToDtoWithoutComments;

import com.project.prepnester.dto.request.SubQuestionDtoRequest;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.dto.response.SubQuestionWithoutCommentsDto;
import com.project.prepnester.model.content.SubQuestion;
import com.project.prepnester.repository.CommentRepository;
import com.project.prepnester.repository.SubQuestionRepository;
import com.project.prepnester.repository.UserRepository;
import com.project.prepnester.util.exceptions.NoPermissionException;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class SubQuestionService {

  private final SubQuestionRepository subQuestionRepository;

  private final UserIdService userIdService;

  private final UserRepository userRepository;

  private final CommentRepository commentRepository;

  @Transactional(readOnly = true)
  public SubQuestionDto getSubQuestionById(UUID subQuestionId) {
    log.info("Fetching sub-questions for question with id: {}", subQuestionId);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub question not found"));

    String createdBy = userRepository.findById(subQuestion.getCreatedBy())
        .orElseThrow(
            () -> new NotFoundException(
                "User with id " + subQuestion.getCreatedBy() + " not found"))
        .getFullName();

    String updatedBy = null;

    if (subQuestion.getUpdatedBy() != null) {
      updatedBy = userRepository.findById(subQuestion.getUpdatedBy())
          .orElseThrow(
              () -> new NotFoundException(
                  "User with id " + subQuestion.getUpdatedBy() + " not found"))
          .getFullName();
    }

    return mapSubQuestionToDto(subQuestion, createdBy, updatedBy,
        commentRepository.findAllBySubQuestionId(subQuestionId));
  }

  public SubQuestionWithoutCommentsDto updateSubQuestion(UUID subQuestionId,
      SubQuestionDtoRequest request) {
    log.info("Updating question with id: {} and title: {}", subQuestionId, request);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub-question not found"));

    subQuestion.setTitle(request.getTitle());
    subQuestion.setUpdatedAt(LocalDateTime.now());

    subQuestion = subQuestionRepository.save(subQuestion);
    return mapSubQuestionToDtoWithoutComments(subQuestion);
  }

  public void deleteSubQuestion(UUID subQuestionId) {
    log.info("Deleting sub-question with id: {}", subQuestionId);

    SubQuestion subQuestion = subQuestionRepository.findById(subQuestionId)
        .orElseThrow(() -> new NotFoundException("Sub-question not found"));

    if (!userIdService.getCurrentUserId().equals(subQuestion.getCreatedBy())) {
      throw new NoPermissionException("User doesn't have permission to update this question");
    }

    subQuestionRepository.delete(subQuestion);
  }

}
