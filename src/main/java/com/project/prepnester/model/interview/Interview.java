package com.project.prepnester.model.interview;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interview")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @OneToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  @Column(name = "open_position", nullable = false)
  private String openPosition;

  @Column(name = "department_name", nullable = false)
  private String departmentName;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "notes")
  private String notes;

  @Column(name = "created_by", nullable = false)
  private UUID createdBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
