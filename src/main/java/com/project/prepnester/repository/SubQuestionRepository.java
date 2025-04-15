package com.project.prepnester.repository;

import com.project.prepnester.model.content.SubQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubQuestionRepository extends JpaRepository<SubQuestion, Long> {

}