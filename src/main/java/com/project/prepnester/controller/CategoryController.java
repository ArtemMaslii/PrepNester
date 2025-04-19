package com.project.prepnester.controller;

import com.project.prepnester.dto.response.CategoryDto;
import com.project.prepnester.service.CategoryService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${custom.api.paths.v1}/categories")
@Validated
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public List<CategoryDto> getCategories() {
    return categoryService.getCategories();
  }

  @GetMapping("/{id}")
  public CategoryDto getCategoryByTitle(@PathVariable UUID id) {
    return categoryService.getCategoryById(id);
  }
}
