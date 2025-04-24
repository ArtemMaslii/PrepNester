package com.project.prepnester.model.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "message", nullable = false)
  private String message;

  @ManyToOne
  @JoinColumn(name = "question_id")
  @JsonIgnore
  private Question question;

  @ManyToOne
  @JoinColumn(name = "sub_question_id")
  @JsonIgnore
  private SubQuestion subQuestion;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @JoinColumn(name = "created_by", nullable = false)
  private UUID createdBy;

  @JoinColumn(name = "updated_by")
  private UUID updatedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  @JsonIgnore
  private Comment parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> replies = new ArrayList<>();
}
