package com.koroli.translator.repository.impl;

import com.koroli.translator.model.User;
import com.koroli.translator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@RequiredArgsConstructor
public class ImplUserRepository implements UserRepository {

    private final Connection connection;

    private final String SAVE_USER = "INSERT INTO account (id) VALUES(?)";
    private final String FIND_USER_BY_ID = "SELECT * FROM account WHERE id = ?";
    private final String DELETE_USER = "DELETE FROM account WHERE id = ?";

    @Override
    @SneakyThrows
    public void save(User user) {
        try {
            findById(user.getId());
        } catch (NullPointerException e) {
            PreparedStatement stmt = connection.prepareStatement(SAVE_USER);
            stmt.setString(1, user.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public User findById(String id) {
        PreparedStatement stmt = connection.prepareStatement(FIND_USER_BY_ID);
        stmt.setString(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return User.builder()
                    .id(rs.getString("id"))
                    .build();
        }

        throw new NullPointerException("Not find USER with ID: " + id);
    }

    @Override
    @SneakyThrows
    public void delete(String id) {
        PreparedStatement stmt = connection.prepareStatement(DELETE_USER);
        stmt.setString(1, id);
        stmt.executeUpdate();
    }
}
