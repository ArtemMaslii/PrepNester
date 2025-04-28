package com.project.prepnester.controller;

import com.project.prepnester.dto.request.CandidateRequestDto;
import com.project.prepnester.dto.request.CandidateUpdateRequestDto;
import com.project.prepnester.dto.request.InterviewUpdateRequestDto;
import com.project.prepnester.dto.response.CandidateDto;
import com.project.prepnester.dto.response.InterviewPreviewDto;
import com.project.prepnester.model.interview.Interview;
import com.project.prepnester.service.CandidateService;
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

  private final CandidateService candidateService;

  private final InterviewService interviewService;

  @GetMapping
  public List<InterviewPreviewDto> getAllInterviews() {
    return interviewService.getAllInterviews();
  }

  @GetMapping("/{id}")
  public Interview getInterviewById(@PathVariable UUID id) {
    return interviewService.getInterviewById(id);
  }

  @PostMapping
  public ResponseEntity<Interview> createInterview(
      @RequestBody Interview interview) {
    return ResponseEntity.ok(interviewService.saveInterview(interview));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Interview> updateInterview(
      @PathVariable UUID id,
      @RequestBody InterviewUpdateRequestDto interview) {
    return ResponseEntity.ok(interviewService.updateInterview(id, interview));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInterview(@PathVariable UUID id) {
    interviewService.deleteInterview(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/candidate")
  public CandidateDto getCandidateByEmail(@RequestParam String email) {
    return candidateService.getCandidateByEmail(email);
  }

  @PostMapping("/candidate")
  public ResponseEntity<CandidateDto> createCandidate(
      @RequestBody CandidateRequestDto candidateDto) {
    return ResponseEntity.ok(candidateService.createCandidate(candidateDto));
  }

  @PutMapping("/candidate")
  public ResponseEntity<CandidateDto> updateCandidate(
      @RequestBody CandidateUpdateRequestDto candidateDto) {
    return ResponseEntity.ok(candidateService.updateCandidate(candidateDto));
  }

  @DeleteMapping("/candidate")
  public ResponseEntity<Void> deleteCandidate(@RequestParam String email) {
    candidateService.deleteCandidate(email);
    return ResponseEntity.noContent().build();
  }
}
