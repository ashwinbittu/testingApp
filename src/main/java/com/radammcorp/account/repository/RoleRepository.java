package com.radammcorp.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radammcorp.account.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
