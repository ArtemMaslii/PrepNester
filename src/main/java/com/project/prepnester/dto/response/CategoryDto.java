package com.project.prepnester.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDto {

  private UUID id;

  private String title;
}
