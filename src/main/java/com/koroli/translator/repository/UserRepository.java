package com.koroli.translator.repository;

import com.koroli.translator.model.User;

public interface UserRepository {
    void save(User user);

    User findById(String id);

    void delete(String id);
}