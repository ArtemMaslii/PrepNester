package com.project.prepnester.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentRequestDto {

  private String message;

  private UUID parentId;

  private UUID createdBy;
}
