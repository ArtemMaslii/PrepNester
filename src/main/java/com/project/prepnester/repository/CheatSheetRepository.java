package com.project.prepnester.repository;

import com.project.prepnester.model.content.CheatSheet;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheatSheetRepository extends JpaRepository<CheatSheet, UUID> {

  List<CheatSheet> findAllByIsPublic(Boolean isPublic, Pageable pageable);

  List<CheatSheet> findAllByIsPublicAndTitleContainingIgnoreCase(Boolean isPublic, String search);
}
