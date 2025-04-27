package com.project.prepnester.model.content;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubQuestion {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_question_id", nullable = false)
  @JsonBackReference
  private Question parentQuestion;

  @OneToMany(mappedBy = "subQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "subQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Like> likes = new ArrayList<>();

  @Column(name = "created_by")
  private UUID createdBy;

  @Column(name = "updated_by")
  private UUID updatedBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}