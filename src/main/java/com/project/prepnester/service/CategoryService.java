package com.project.prepnester.service;

import com.project.prepnester.dto.response.CategoryDto;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.service.mapper.CategoryToCategoryDtoMapper;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<CategoryDto> getCategories() {
    log.info("Fetching all categories from the database");
    return categoryRepository
        .findAll()
        .stream()
        .map(CategoryToCategoryDtoMapper.INSTANCE::categoryToCategoryDto).toList();
  }

  public CategoryDto getCategoryById(UUID id) {
    log.info("Fetching category with id: {}", id);
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Category not found"));

    return CategoryToCategoryDtoMapper.INSTANCE.categoryToCategoryDto(category);
  }
}
