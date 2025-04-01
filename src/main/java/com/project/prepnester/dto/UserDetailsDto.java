package com.project.prepnester.dto;

import com.project.prepnester.model.Gender;
import lombok.Getter;

@Getter
public class UserDetailsDto {

  private String fullName;

  private String email;

  private String passwordHash;

  private String phoneNumber;

  private Gender gender;
}
