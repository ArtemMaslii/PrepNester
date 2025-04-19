package com.project.prepnester.service;

import com.project.prepnester.dto.response.CategoryDto;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

  List<CategoryDto> getCategories();

  CategoryDto getCategoryById(UUID id);
}
