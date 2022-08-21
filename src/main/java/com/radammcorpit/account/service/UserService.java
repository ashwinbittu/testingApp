package com.radammcorpit.account.service;

import java.util.List;

import com.radammcorpit.account.model.User;

/** {@author waheedk}!*/
public interface UserService {
	/** {@inheritDoc}} !*/
    void save(User user);
    /** {@inheritDoc}} !*/
    User findByUsername(String username);
    User findById(long id);
    /*public void updateUser(User user);*/
    public List <User> getList();
}
