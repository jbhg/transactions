package com.joelbgreenberg.db.inmemorydb;

import com.joelbgreenberg.db.ITransactionalDatabase;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryDatabase implements ITransactionalDatabase {

    private final AtomicBoolean isAcceptingCommands = new AtomicBoolean(true);
    private final Deque<ActionsInTransaction> transactions = new ArrayDeque<>();

    public InMemoryDatabase() {
        transactions.push(new ActionsInTransaction()); // The "base" data.
    }

    @Override
    public void set(String name, String value) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        transactions.getLast().set(name, value);
    }

    @Override
    public Optional<String> get(String name) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        return transactions.stream()
                .filter(s -> s.get(name).isPresent() )
                .map(s -> s.get(name).get())
                .findFirst();
    }

    @Override
    public void delete(String name) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        transactions.getLast().delete(name);
    }

    @Override
    public long count(String value) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        return transactions.getFirst().count(value);
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
        transactions.push(new ActionsInTransaction());
    }

    @Override
    public Optional<Integer> rollbackTransaction() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        if (transactions.size() == 1) {
            return Optional.empty();
        }
        transactions.pop();
        return Optional.of(transactions.size());
    }

    @Override
    public void commit() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        throw new RuntimeException("Not implemented.");
    }
}
