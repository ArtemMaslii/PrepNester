package com.project.prepnester.model.userDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Role {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "access_type", nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private AccessType accessType;
}
