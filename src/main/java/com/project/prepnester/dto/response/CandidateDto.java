package com.project.prepnester.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CandidateDto {

  private UUID id;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String rawPassword;

  private CheatSheetDto cheatSheet;
}
