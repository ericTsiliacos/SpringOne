package io.pivotal.notes.repositories;

import io.pivotal.notes.models.User;

public interface UserRepository {

    User saveOrUpdateUser(Integer id, String username, String password);
    User getUserById(int id);
}
