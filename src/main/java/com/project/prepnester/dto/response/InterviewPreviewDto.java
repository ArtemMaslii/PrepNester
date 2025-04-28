package com.project.prepnester.dto.response;

import com.project.prepnester.model.interview.Status;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class InterviewPreviewDto {

  private UUID id;

  private String openPosition;

  private String candidateFullName;

  private Status status;

  private LocalDateTime createdAt;
}
