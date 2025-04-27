package com.project.prepnester.model.content;

import com.project.prepnester.model.userDetails.PrepNesterUserDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private PrepNesterUserDetails user;

  @ManyToOne
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  @ManyToOne
  @JoinColumn(name = "sub_question_id", nullable = false)
  private SubQuestion subQuestion;

  @ManyToOne
  @JoinColumn(name = "comment_id", nullable = false)
  private Comment comment;

  @ManyToOne
  @JoinColumn(name = "cheat_sheet_id", nullable = false)
  private CheatSheet cheatSheet;
}
