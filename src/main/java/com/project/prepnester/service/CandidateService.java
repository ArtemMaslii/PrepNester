package com.project.prepnester.service;

import com.project.prepnester.dto.request.CandidateRequestDto;
import com.project.prepnester.dto.request.CandidateUpdateRequestDto;
import com.project.prepnester.dto.response.CandidateDto;
import com.project.prepnester.dto.response.CheatSheetDto;
import com.project.prepnester.model.interview.Candidate;
import com.project.prepnester.repository.CandidateRepository;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CandidateService {

  private final CandidateRepository candidateRepository;

  private final CheatSheetService cheatSheetService;

  private final PasswordEncoder passwordEncoder;


  public CandidateDto getCandidateByEmail(String email) {
    Candidate candidate = candidateRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Candidate not found"));

    return getCandidateDto(candidate);
  }

  public CandidateDto createCandidate(CandidateRequestDto request) {
    Candidate candidate = Candidate.builder()
        .fullName(request.getFullName())
        .email(request.getEmail())
        .phoneNumber(request.getPhoneNumber())
        .passwordHash(passwordEncoder.encode(request.getRawPassword()))
        .rawPassword(request.getRawPassword())
        .createdBy(request.getCreatedBy())
        .createdAt(LocalDateTime.now())
        .build();

    Candidate saved = candidateRepository.save(candidate);

    return getCandidateDto(saved);
  }

  public CandidateDto updateCandidate(CandidateUpdateRequestDto request) {
    Candidate candidate = candidateRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new NotFoundException("Candidate not found"));

    candidate.setFullName(request.getFullName());
    candidate.setEmail(request.getEmail());
    candidate.setPhoneNumber(request.getPhoneNumber());

    Candidate saved = candidateRepository.save(candidate);

    return getCandidateDto(saved);
  }

  public void deleteCandidate(String email) {
    Candidate candidate = candidateRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Candidate not found"));

    candidateRepository.delete(candidate);
  }

  private CandidateDto getCandidateDto(Candidate candidate) {
    return CandidateDto.builder()
        .id(candidate.getId())
        .fullName(candidate.getFullName())
        .email(candidate.getEmail())
        .phoneNumber(candidate.getPhoneNumber())
        .rawPassword(candidate.getRawPassword())
        .cheatSheet(
            CheatSheetDto.builder()
                .id(candidate.getCheatSheet().getId())
                .title(candidate.getCheatSheet().getTitle())
                .categories(cheatSheetService.getCheatSheetsCategories(candidate.getCheatSheet()))
                .createdAt(candidate.getCheatSheet().getCreatedAt())
                .updatedAt(candidate.getCheatSheet().getUpdatedAt())
                .createdBy(candidate.getCheatSheet().getCreatedBy())
                .updatedBy(candidate.getCheatSheet().getUpdatedBy())
                .build()
        )
        .build();
  }
}
