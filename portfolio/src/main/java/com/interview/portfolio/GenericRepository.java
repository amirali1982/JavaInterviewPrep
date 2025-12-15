package com.interview.portfolio;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface using Bounded Type Parameters.
 * Restricts T to be a subclass of Asset.
 *
 * @param <T> the type of asset, must extend Asset
 */
public interface GenericRepository<T extends Asset> {

    void save(T item);

    Optional<T> findById(String id);

    List<T> findAll();
}
