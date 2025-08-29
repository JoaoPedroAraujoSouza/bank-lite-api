package com.banklite.bankliteapi.repository;

import com.banklite.bankliteapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
