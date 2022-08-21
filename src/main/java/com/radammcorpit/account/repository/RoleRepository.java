package com.radammcorpit.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radammcorpit.account.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
