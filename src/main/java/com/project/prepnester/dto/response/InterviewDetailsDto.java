package com.project.prepnester.dto.response;

import com.project.prepnester.model.interview.Status;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class InterviewDetailsDto {

  private UUID id;

  private CandidateDetailsDto candidate;

  private String openPosition;

  private String department;

  private Status status;

  private String notes;

  private UUID cheatSheetId;
}
