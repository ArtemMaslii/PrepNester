package com.project.prepnester.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortBy {
  ASCENDING("ASC"),
  DESCENDING("DESC"),
  MOST_LIKED("MOST_LIKED"),
  MOST_COMMENTED("MOST_COMMENTED"),
  RECENTLY_CREATED("RECENTLY_CREATED"),
  RECENTLY_UPDATED("RECENTLY_UPDATED");

  private final String value;
}
