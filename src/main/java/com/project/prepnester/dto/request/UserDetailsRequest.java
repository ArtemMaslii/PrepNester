package com.project.prepnester.dto.request;

import com.project.prepnester.model.Gender;
import lombok.Getter;

@Getter
public class UserDetailsRequest {

  private String fullName;

  private String email;

  private String passwordHash;

  private String phoneNumber;

  private Gender gender;
}
