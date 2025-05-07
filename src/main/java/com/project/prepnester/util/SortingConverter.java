package com.project.prepnester.util;

import com.project.prepnester.model.common.SortBy;
import org.springframework.data.domain.Sort;

public class SortingConverter {

  public static Sort getSort(SortBy sortBy) {
    return switch (sortBy) {
      case ASCENDING -> Sort.by(Sort.Direction.ASC, "title");
      case DESCENDING -> Sort.by(Sort.Direction.DESC, "title");
      case RECENTLY_CREATED -> Sort.by(Sort.Direction.DESC, "createdAt");
      case RECENTLY_UPDATED -> Sort.by(Sort.Direction.DESC, "updatedAt");
      default -> Sort.unsorted();
    };
  }

}
