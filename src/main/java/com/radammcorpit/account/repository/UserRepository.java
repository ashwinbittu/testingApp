package com.radammcorpit.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radammcorpit.account.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(long id);
    /*public void updateUser(User user)*/;
    
}
