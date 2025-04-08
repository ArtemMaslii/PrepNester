package com.project.prepnester.service;

import com.project.prepnester.dto.request.UserDetailsRequest;
import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.model.PrepNesterUserDeatils;

public interface UserDetailsService {

  UserDetailsResponse getUserDetails(String email);

  PrepNesterUserDeatils registerUser(UserDetailsRequest userDetailsDto);
}
