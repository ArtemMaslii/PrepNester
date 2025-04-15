package com.project.prepnester.service;

import com.project.prepnester.dto.request.CreateQuestionBodyRequest;
import com.project.prepnester.dto.request.PageInfoDto;
import com.project.prepnester.dto.response.QuestionDto;
import com.project.prepnester.model.SortBy;
import java.util.List;

public interface QuestionService {

  List<QuestionDto> getAllQuestions(PageInfoDto pageInfoDto, SortBy sortBy, Boolean isPublic);

  QuestionDto createQuestion(CreateQuestionBodyRequest body);
}
