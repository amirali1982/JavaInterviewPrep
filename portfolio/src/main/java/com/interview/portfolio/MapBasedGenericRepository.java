package com.interview.portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MapBasedGenericRepository<T> implements GenericRepository<T> {
    private final Map<String, T> storage = new HashMap<>();
    private final Map<Class<?>, List<T>> assetRegistry = new HashMap<>();

    @Override
    public void save(String id, T entity) {
        storage.put(id, entity);
        // Also add to registry for consistency
        add(entity);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(T entity) {
        Class<?> clazz = entity.getClass();
        List<T> assets = assetRegistry.computeIfAbsent(clazz, k -> new ArrayList<>());
        assets.add(entity);

        // Try to sync with ID storage if possible
        if (entity instanceof Asset) {
            Asset asset = (Asset) entity;
            storage.put(asset.getSymbol(), entity);
        }
    }
}
