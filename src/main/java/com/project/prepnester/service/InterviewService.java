package com.project.prepnester.service;

import com.project.prepnester.dto.request.InterviewCreateRequest;
import com.project.prepnester.dto.request.InterviewUpdateRequestDto;
import com.project.prepnester.dto.response.CandidateDetailsDto;
import com.project.prepnester.dto.response.InterviewDetailsDto;
import com.project.prepnester.dto.response.InterviewPreviewDto;
import com.project.prepnester.model.content.CheatSheet;
import com.project.prepnester.model.interview.Candidate;
import com.project.prepnester.model.interview.Interview;
import com.project.prepnester.model.interview.Status;
import com.project.prepnester.repository.CandidateRepository;
import com.project.prepnester.repository.CheatSheetRepository;
import com.project.prepnester.repository.InterviewRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class InterviewService {

  private final InterviewRepository interviewRepository;

  private final CandidateRepository candidateRepository;

  private final CheatSheetRepository cheatSheetRepository;

  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<InterviewPreviewDto> getAllInterviews(String search, Status status) {
    List<Interview> interviews;

    if (search != null && !search.isBlank()) {
      if (status != null) {
        interviews = interviewRepository.findAllByCandidate_FullNameContainingIgnoreCaseAndStatus(
            search, status);
      } else {
        interviews = interviewRepository.findAllByCandidate_FullNameContainingIgnoreCase(search);
      }
    } else {
      if (status != null) {
        interviews = interviewRepository.findAllByStatus(status);
      } else {
        interviews = interviewRepository.findAll();
      }
    }

    return interviews.stream()
        .map(interview -> InterviewPreviewDto.builder()
            .id(interview.getId())
            .openPosition(interview.getOpenPosition())
            .candidateFullName(interview.getCandidate().getFullName())
            .status(interview.getStatus())
            .createdAt(interview.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public InterviewDetailsDto getInterviewById(UUID id) {
    Interview interview = interviewRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Interview with id " + id + " not found"));

    Candidate candidate = interview.getCandidate();

    return getInterviewDetails(interview, candidate);
  }

  public InterviewDetailsDto saveInterview(InterviewCreateRequest body) {
    String rawPassword = UUID.randomUUID().toString();

    Candidate toSaveCandidate = Candidate.builder()
        .fullName(body.getCandidateFullName())
        .email(body.getEmail())
        .phoneNumber(body.getPhoneNumber())
        .rawPassword(rawPassword)
        .passwordHash(passwordEncoder.encode(rawPassword))
        .cheatSheet(null)
        .createdBy(body.getCreatedBy())
        .createdAt(LocalDateTime.now())
        .build();

    Interview toSaveInterview = Interview.builder()
        .candidate(toSaveCandidate)
        .status(Status.IN_PROGRESS)
        .openPosition(body.getOpenPosition())
        .departmentName(body.getDepartment())
        .notes(body.getNotes())
        .createdBy(body.getCreatedBy())
        .createdAt(LocalDateTime.now())
        .build();

    Interview saved = interviewRepository.save(toSaveInterview);

    return getInterviewDetails(saved, saved.getCandidate());
  }

  public InterviewDetailsDto updateInterview(UUID id, InterviewUpdateRequestDto toEdit) {
    Interview interview = interviewRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Interview with id " + id + " not found"));

    Candidate candidate = candidateRepository.findById(interview.getCandidate().getId())
        .orElseThrow(() -> new NotFoundException(
            "Candidate with id " + interview.getCandidate().getId() + " not found"));

    interview.setOpenPosition(toEdit.getOpenPosition());
    interview.setDepartmentName(toEdit.getDepartmentName());
    interview.setStatus(toEdit.getStatus());
    interview.setNotes(toEdit.getNotes());
    if (toEdit.getCandidate() != null) {
      candidate.setFullName(toEdit.getCandidate().getFullName());
      candidate.setEmail(toEdit.getCandidate().getEmail());
      candidate.setPhoneNumber(toEdit.getCandidate().getPhoneNumber());

      if (toEdit.getCheatSheetId() != null) {
        CheatSheet cheatSheet = cheatSheetRepository.findById(toEdit.getCheatSheetId())
            .orElseThrow(() -> new NotFoundException(
                "Candidate with id " + toEdit.getCheatSheetId() + " not found"));

        candidate.setCheatSheet(cheatSheet);
      }

      interview.setCandidate(candidate);
    }

    Interview saved = interviewRepository.save(interview);

    return getInterviewDetails(saved, saved.getCandidate());
  }

  public void deleteInterview(UUID id) {
    Interview interview = interviewRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Interview with id " + id + " not found"));

    interviewRepository.delete(interview);
  }

  private InterviewDetailsDto getInterviewDetails(Interview interview, Candidate candidate) {
    return InterviewDetailsDto.builder()
        .id(interview.getId())
        .candidate(CandidateDetailsDto.builder()
            .fullName(candidate.getFullName())
            .email(candidate.getEmail())
            .phoneNumber(candidate.getPhoneNumber())
            .rawPassword(candidate.getRawPassword())
            .build())
        .openPosition(interview.getOpenPosition())
        .department(interview.getDepartmentName())
        .status(interview.getStatus())
        .notes(interview.getNotes())
        .cheatSheetId(candidate.getCheatSheet() != null ? candidate.getCheatSheet().getId() : null)
        .build();
  }
}
