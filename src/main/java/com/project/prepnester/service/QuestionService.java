package com.project.prepnester.service;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.request.SubQuestionDtoRequest;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.dto.response.SubQuestionDto;
import com.project.prepnester.model.common.SortBy;
import java.util.List;
import java.util.UUID;

public interface QuestionService {

  List<QuestionDto> getAllQuestions(PageInfoDto pageInfoDto, SortBy sortBy, Boolean isPublic);

  QuestionDto getQuestionById(UUID questionId);

  SubQuestionDto getSubQuestionById(UUID subQuestionId);

  QuestionDto createQuestion(CreateQuestionBodyRequest body);

  QuestionDto updateQuestion(UUID questionId, CreateQuestionBodyRequest body);

  SubQuestionDto updateSubQuestion(UUID subQuestionId, SubQuestionDtoRequest request);

  void deleteQuestion(UUID questionId);

  void deleteSubQuestion(UUID subQuestionId);
}
