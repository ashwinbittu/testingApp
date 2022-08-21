package com.radammcorp.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radammcorp.account.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
