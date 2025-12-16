package com.interview.portfolio.repository;

import java.util.Optional;

public interface GenericRepository<T> {
    Optional<T> findById(String id);

    void save(String id, T entity);

    @com.interview.portfolio.proxy.Logged
    void add(T entity);
}
