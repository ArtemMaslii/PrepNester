package com.project.prepnester.model.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cheat_sheet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheatSheet {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "title", nullable = false)
  private String title;

  @ManyToMany
  @JoinTable(
      name = "categories_cheat_sheets",
      joinColumns = @JoinColumn(name = "cheat_sheet_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Category> categories;

  @ManyToMany
  @JoinTable(
      name = "questions_cheat_sheets",
      joinColumns = @JoinColumn(name = "cheat_sheet_id"),
      inverseJoinColumns = @JoinColumn(name = "question_id")
  )
  private List<Question> questions;

  @Column(name = "is_public", nullable = false)
  private Boolean isPublic;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "created_by")
  private UUID createdBy;

  @Column(name = "updated_by")
  private UUID updatedBy;
}
