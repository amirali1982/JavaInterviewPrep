package com.interview.portfolio;

import java.util.Optional;

public interface GenericRepository<T> {
    Optional<T> findById(String id);

    void save(String id, T entity);

    void add(T entity);
}
