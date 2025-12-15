package com.interview.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe implementation of GenericRepository.
 * Uses ConcurrentHashMap for storage.
 *
 * @param <T> the type of asset
 */
public class MapBasedGenericRepository<T extends Asset> implements GenericRepository<T> {

    private final Map<String, T> storage = new ConcurrentHashMap<>();

    @Override
    public void save(T item) {
        storage.put(item.getSymbol(), item);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
}
