package com.project.prepnester.model.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "title", nullable = false)
  private String title;
}
