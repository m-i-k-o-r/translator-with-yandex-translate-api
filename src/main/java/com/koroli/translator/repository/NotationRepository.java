package com.koroli.translator.repository;

import com.koroli.translator.model.Notation;

import java.util.Set;
import java.util.UUID;

public interface NotationRepository {
    void save(Notation notation, String userId);

    Notation findById(UUID id);

    Set<Notation> findAllByUser(String userId);

    void delete(UUID id);
}
