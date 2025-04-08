package com.project.prepnester.service;

import com.project.prepnester.dto.request.LoginDto;

public interface AuthService {

  String login(LoginDto loginDto);
}
