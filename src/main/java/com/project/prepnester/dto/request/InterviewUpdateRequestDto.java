package com.project.prepnester.dto.request;

import com.project.prepnester.model.interview.Candidate;
import com.project.prepnester.model.interview.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class InterviewUpdateRequestDto {

  private Candidate candidate;

  private String openPosition;

  private String departmentName;

  private Status status;

  private String notes;
}
