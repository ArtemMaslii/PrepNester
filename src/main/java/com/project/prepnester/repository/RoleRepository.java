package com.project.prepnester.repository;

import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByAccessType(AccessType roleType);
}
