package com.koroli.translator.repository.impl;

import com.koroli.translator.model.Notation;
import com.koroli.translator.repository.NotationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class implNotationRepository implements NotationRepository {

    private final Connection connection;

    private final String SAVE_NOTATION = "INSERT INTO notation (source_text, target_text, user_id) VALUES(?, ?, ?)";
    private final String FIND_NOTATION_BY_ID = "SELECT * FROM notation WHERE id = uuid(?)";
    private final String FIND_ALL_NOTATIONS = "SELECT * FROM notation WHERE user_id = ?";
    private final String DELETE_NOTATION = "DELETE FROM notation WHERE id = uuid(?)";

    @Override
    @SneakyThrows
    public void save(Notation notation, String userId) {
        try {
            if (notation.getId() == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            PreparedStatement stmt = connection.prepareStatement(SAVE_NOTATION);
            stmt.setString(1, notation.getSourceText());
            stmt.setString(2, notation.getTargetText());
            stmt.setString(3, userId);

            stmt.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public Notation findById(UUID id) {
        PreparedStatement stmt = connection.prepareStatement(FIND_NOTATION_BY_ID);
        stmt.setString(1, String.valueOf(id));

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return Notation.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .sourceText(rs.getString("source_text"))
                    .targetText(rs.getString("target_text"))
                    .build();
        }

        throw new NullPointerException("Not find NOTATION with ID: " + id);
    }

    @Override
    @SneakyThrows
    public Set<Notation> findAllByUser(String userId) {
        PreparedStatement stmt = connection.prepareStatement(FIND_ALL_NOTATIONS);
        stmt.setString(1, userId);

        ResultSet rs = stmt.executeQuery();

        Set<Notation> res = new HashSet<>();
        while (rs.next()) {
            res.add(
                    Notation.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .sourceText(rs.getString("source_text"))
                            .targetText(rs.getString("target_text"))
                            .build()
            );
        }
        return res;
    }

    @Override
    @SneakyThrows
    public void delete(UUID id) {
        PreparedStatement stmt = connection.prepareStatement(DELETE_NOTATION);
        stmt.setString(1, String.valueOf(id));
        stmt.executeUpdate();
    }
}
