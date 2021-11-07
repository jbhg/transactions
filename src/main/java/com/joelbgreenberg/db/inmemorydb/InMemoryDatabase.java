package com.joelbgreenberg.db.inmemorydb;

import com.joelbgreenberg.db.IDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryDatabase implements IDatabase {

    private final Map<String, String> values = new HashMap<>();
    private final AtomicBoolean isAcceptingCommands = new AtomicBoolean(true);

    @Override
    public void set(String name, String value) {
        values.put(name, value);
    }

    @Override
    public Optional<String> get(String name) {
        return Optional.ofNullable(values.get(name));
    }

    @Override
    public void delete(String name) {
        values.remove(name);
    }

    @Override
    public int count(String value) {
        return (int) values.values().stream().filter(v -> v.equals(value)).count();
    }

    @Override
    public void end() {
        this.isAcceptingCommands.set(false);
    }

    @Override
    public void beginTransaction() {

    }

    @Override
    public Optional<Integer> rollbackTransaction() {
        return Optional.empty();
    }

    @Override
    public void commit() {

    }
}
