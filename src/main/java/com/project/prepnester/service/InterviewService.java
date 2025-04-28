package com.project.prepnester.service;

import com.project.prepnester.dto.request.CandidateUpdateRequestDto;
import com.project.prepnester.dto.request.InterviewUpdateRequestDto;
import com.project.prepnester.dto.response.InterviewPreviewDto;
import com.project.prepnester.model.interview.Interview;
import com.project.prepnester.repository.InterviewRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class InterviewService {

  private final InterviewRepository interviewRepository;

  private final CandidateService candidateService;

  @Transactional(readOnly = true)
  public List<InterviewPreviewDto> getAllInterviews() {
    return interviewRepository.findAll()
        .stream()
        .map(interview -> InterviewPreviewDto.builder()
            .id(interview.getId())
            .openPosition(interview.getOpenPosition())
            .candidateFullName(interview.getCandidate().getFullName())
            .status(interview.getStatus())
            .createdAt(interview.getCreatedAt())
            .build())
        .toList();
  }

  @Transactional(readOnly = true)
  public Interview getInterviewById(UUID id) {
    return interviewRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Interview with id " + id + " not found"));
  }

  public Interview saveInterview(Interview interview) {
    return interviewRepository.save(interview);
  }

  public Interview updateInterview(UUID id, InterviewUpdateRequestDto toEdit) {
    Interview interview = interviewRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Interview with id " + id + " not found"));

    candidateService.updateCandidate(
        CandidateUpdateRequestDto.builder()
            .fullName(toEdit.getCandidate().getFullName())
            .email(toEdit.getCandidate().getEmail())
            .phoneNumber(toEdit.getCandidate().getPhoneNumber())
            .build()
    );
    interview.setOpenPosition(toEdit.getOpenPosition());
    interview.setDepartmentName(toEdit.getDepartmentName());
    interview.setStatus(toEdit.getStatus());
    interview.setNotes(toEdit.getNotes());

    return interviewRepository.save(interview);
  }

  public void deleteInterview(UUID id) {
    Interview interview = interviewRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Interview with id " + id + " not found"));
    interviewRepository.delete(interview);
  }
}
