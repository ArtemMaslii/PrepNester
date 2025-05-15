package com.project.prepnester.controller;

import com.project.prepnester.dto.request.InterviewCreateRequest;
import com.project.prepnester.dto.request.InterviewUpdateRequestDto;
import com.project.prepnester.dto.response.InterviewDetailsDto;
import com.project.prepnester.dto.response.InterviewPreviewDto;
import com.project.prepnester.model.interview.Status;
import com.project.prepnester.service.InterviewService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/interview")
public class InterviewController {

  private final InterviewService interviewService;

  @GetMapping
  public List<InterviewPreviewDto> getAllInterviews(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String status
  ) {
    Status statusEnum = null;
    if (status != null && !status.isEmpty()) {
      statusEnum = Status.fromValue(status);
    }
    return interviewService.getAllInterviews(search, statusEnum);
  }

  @GetMapping("/{id}")
  public InterviewDetailsDto getInterviewById(@PathVariable UUID id) {
    return interviewService.getInterviewById(id);
  }

  @PostMapping
  public ResponseEntity<InterviewDetailsDto> createInterview(
      @RequestBody InterviewCreateRequest interview) {
    return ResponseEntity.ok(interviewService.saveInterview(interview));
  }

  @PutMapping("/{id}")
  public ResponseEntity<InterviewDetailsDto> updateInterview(
      @PathVariable UUID id,
      @RequestBody InterviewUpdateRequestDto interview) {
    return ResponseEntity.ok(interviewService.updateInterview(id, interview));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInterview(@PathVariable UUID id) {
    interviewService.deleteInterview(id);
    return ResponseEntity.noContent().build();
  }
}
