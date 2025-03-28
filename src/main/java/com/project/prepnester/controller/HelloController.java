package com.project.prepnester.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${custom.api.paths.v1}/hello")
public class HelloController {

  @GetMapping
  public String hello() {
    return "Hello, World!";
  }
}
