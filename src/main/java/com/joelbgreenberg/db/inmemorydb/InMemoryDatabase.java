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
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        values.put(name, value);
    }

    @Override
    public Optional<String> get(String name) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        return Optional.ofNullable(values.get(name));
    }

    @Override
    public void delete(String name) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        values.remove(name);
    }

    @Override
    public int count(String value) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        return (int) values.values().stream().filter(v -> v.equals(value)).count();
    }

    @Override
    public void end() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        this.isAcceptingCommands.set(false);
    }

    @Override
    public void beginTransaction() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
    }

    @Override
    public Optional<Integer> rollbackTransaction() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        return Optional.empty();
    }

    @Override
    public void commit() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
    }
}
