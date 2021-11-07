package com.joelbgreenberg.db.inmemorydb;

import com.google.common.collect.MapMaker;
import com.joelbgreenberg.db.IDatabase;

import java.util.Map;
import java.util.Optional;

public class ActionsInTransaction implements IDatabase {

    private final Map<String, Optional<String>> mappings = new MapMaker().makeMap();

    @Override
    public void set(String name, String value) {
        mappings.put(name, Optional.of(value));
    }

    @Override
    public Optional<String> get(String name) {
        if (!mappings.containsKey(name)) {
            return Optional.empty();
        }
        return mappings.get(name);
    }

    @Override
    public void delete(String name) {
        mappings.put(name, Optional.empty());
    }

    @Override
    public long count(String value) {
        return mappings.values()
                .stream()
                .filter(Optional::isPresent)
                .filter(str -> str.get().equals(value))
                .count();
    }

    @Override
    public void end() {

    }
}
