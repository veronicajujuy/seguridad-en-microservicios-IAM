package com.dh.usersservice.repository;

import java.util.List;
import java.util.Optional;

import com.dh.usersservice.model.User;
import com.dh.usersservice.model.UserRequest;

public interface IUserRepository {

  Optional<User> findById(String id);

  List<User> findByUsername(String username);

  User createUser(UserRequest userRequest);
}
