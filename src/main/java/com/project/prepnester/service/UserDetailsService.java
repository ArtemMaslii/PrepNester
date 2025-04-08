package com.project.prepnester.service;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;

public interface UserDetailsService {

  UserDetailsResponse getUserDetails(String email);

  UserDetailsResponse registerUser(UserDetailsRequest userDetailsDto);
}
