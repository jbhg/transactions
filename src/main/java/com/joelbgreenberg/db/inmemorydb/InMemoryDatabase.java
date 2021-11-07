package com.joelbgreenberg.db.inmemorydb;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.joelbgreenberg.db.IDatabase;
import com.joelbgreenberg.db.ITransactionalDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An in-memory database implementation.
 */
public class InMemoryDatabase implements ITransactionalDatabase {

    private static Logger LOG = LoggerFactory
            .getLogger(InMemoryDatabase.class);

private final AtomicInteger nextTransactionId = new AtomicInteger(0);
    private final AtomicBoolean isAcceptingCommands = new AtomicBoolean(true);
    private final Deque<ActionsInTransaction> transactions = new ArrayDeque<>();

    public InMemoryDatabase() {
        transactions.push(new ActionsInTransaction("BASE")); // The "base" data.
    }

    /**
     * Runtime: O(1) -- setting a new value in the "top" transaction on the
     * stack is a constant time operation.
     *
     * This assumes that the map's key hashing operation is a O(1) lookup.
     *
     * @param name
     * @param value
     */
    @Override
    public void set(String name, String value) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        transactions.getFirst().set(name, value);
    }

    /**
     * Runtime: O(transaction stack count) -- looking up a value is a O(1)
     * operation over *each* transaction in the stack.
     *
     * This assumes that the map's key hashing operation is a O(1) lookup.
     *
     * @param name
     * @return
     */
    @Override
    public Optional<String> get(String name) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        for (ActionsInTransaction c : transactions) {
            if (c.entryMap().containsKey(name)) {
                return c.get(name);
            }
        }
        return Optional.empty();
    }

    /**
     * Runtime: O(1) -- setting a new value in the "top" transaction on the
     * stack is a constant time operation. (In this case, deletion is the same
     * as setting the value to "empty".)
     *
     * This assumes that the map's key hashing operation is a O(1) lookup.
     *
     * @param name
     */
    @Override
    public void delete(String name) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        transactions.getFirst().delete(name);
    }

    /**
     * Runtime: O(transaction stack count) -- looking up a value is a O(1)
     * operation over *each* transaction in the stack.
     *
     * This assumes that determining these counts is a constant operation.
     *
     * @param value
     * @return
     */
    @Override
    public long count(String value) {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }

        Set<String> presenceKeys = new HashSet<>();
        Iterator<ActionsInTransaction> iter = transactions.descendingIterator(); // need to iterate base-first.
        while ( iter.hasNext() ) {
            final ActionsInTransaction tx = iter.next();
            presenceKeys.addAll(tx.keys(value)); // add all additions.
            tx.entryMap().entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isPresent())
                .map(Map.Entry::getKey)
                .forEach(presenceKeys::remove); // then remove all deletions.
        }
        return presenceKeys.size();
    }

    @Override
    public ImmutableSet<String> keys(String value) {
        throw new IllegalArgumentException("Not the right time to use this!");
    }

    @Override
    public ImmutableMap<String, Optional<String>> entryMap() {
        throw new IllegalArgumentException("Not the right time to use this!");
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
        LOG.info("BEGIN TRANSACTION");
        transactions.push(new ActionsInTransaction("TX" + nextTransactionId.incrementAndGet()));
    }

    @Override
    public Optional<Integer> rollbackTransaction() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        if (transactions.size() == 1) {
            return Optional.empty();
        }
        LOG.info("ROLLBACK TRANSACTION");
        transactions.pop();
        return Optional.of(transactions.size());
    }

    @Override
    public void commit() {
        if (!isAcceptingCommands.get()) {
            throw new RuntimeException("This database connection is closed.");
        }
        while (transactions.size() > 1) {
            LOG.info("COMMIT TRANSACTION");
            IDatabase dataToCommit = transactions.pop();
            for (Map.Entry<String, Optional<String>> entry : dataToCommit.entryMap().entrySet()) {
                if (entry.getValue().isPresent()) {
                    this.set(entry.getKey(), entry.getValue().get());
                } else {
                    this.delete(entry.getKey());
                }
            }
        }
    }
}
