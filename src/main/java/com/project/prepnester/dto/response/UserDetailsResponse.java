package com.project.prepnester.dto.response;

import com.project.prepnester.model.userDetails.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

  private String fullName;

  private String email;

  private String phoneNumber;

  private Gender gender;

}
