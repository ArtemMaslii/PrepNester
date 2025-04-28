package com.project.prepnester.model.interview;

import com.project.prepnester.model.content.CheatSheet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "raw_password", nullable = false)
  private String rawPassword;

  @ManyToOne
  @JoinColumn(name = "cheat_sheet_id")
  private CheatSheet cheatSheet;

  @Column(name = "created_by", nullable = false)
  private UUID createdBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
