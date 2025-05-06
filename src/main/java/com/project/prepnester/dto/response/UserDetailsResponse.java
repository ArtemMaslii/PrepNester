package com.project.prepnester.dto.response;

import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.Gender;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDetailsResponse {

  private UUID id;

  private String fullName;

  private String email;

  private String phoneNumber;

  private Gender gender;

  private AccessType role;
}
