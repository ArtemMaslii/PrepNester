package com.project.prepnester.dto.request;

import com.project.prepnester.model.userDetails.Gender;
import com.project.prepnester.validation.GenderConstraint;
import com.project.prepnester.validation.ValidEmail;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsRequest {

  @NotNull(message = "Full name cannot be null")
  @NotEmpty(message = "Full name cannot be empty")
  private String fullName;

  @ValidEmail
  @NotNull(message = "Email cannot be null")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotNull(message = "Password cannot be null")
  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 8, message = "Password should have at least 8 characters")
  private String password;

  @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must match the valid pattern")
  @Nullable
  private String phoneNumber;

  @NotNull(message = "Gender cannot be null")
  @GenderConstraint
  private Gender gender;
}
