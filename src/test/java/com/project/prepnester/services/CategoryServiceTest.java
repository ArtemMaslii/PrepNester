package com.project.prepnester.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.prepnester.dto.response.CategoryDto;
import com.project.prepnester.model.content.Category;
import com.project.prepnester.repository.CategoryRepository;
import com.project.prepnester.service.CategoryService;
import com.project.prepnester.service.mapper.CategoryToCategoryDtoMapper;
import com.project.prepnester.util.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  private Category category;
  private CategoryDto categoryDto;

  @BeforeEach
  void setUp() {
    UUID id = UUID.randomUUID();
    category = Category.builder()
        .id(id)
        .title("Programming")
        .build();

    categoryDto = CategoryToCategoryDtoMapper.INSTANCE.categoryToCategoryDto(category);
  }

  @Test
  void testGetCategories_Success() {
    // Given
    when(categoryRepository.findAll()).thenReturn(List.of(category));

    // When
    List<CategoryDto> result = categoryService.getCategories();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(categoryDto.getId(), result.get(0).getId());
  }

  @Test
  void testGetCategoryById_Success() {
    // Given
    UUID categoryId = category.getId();
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    // When
    CategoryDto result = categoryService.getCategoryById(categoryId);

    // Then
    assertNotNull(result);
    assertEquals(categoryDto.getId(), result.getId());
    assertEquals(categoryDto.getTitle(), result.getTitle());
    verify(categoryRepository, times(1)).findById(categoryId);
  }

  @Test
  void testGetCategoryById_NotFound() {
    // Given
    UUID randomId = UUID.randomUUID();
    when(categoryRepository.findById(randomId)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(randomId));
    verify(categoryRepository, times(1)).findById(randomId);
  }
}