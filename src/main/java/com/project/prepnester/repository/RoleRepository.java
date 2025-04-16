package com.project.prepnester.repository;

import com.project.prepnester.model.userDetails.AccessType;
import com.project.prepnester.model.userDetails.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

  Role findByAccessType(AccessType roleType);
}
