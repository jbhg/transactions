package com.joelbgreenberg.db.inmemorydb;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MapMaker;
import com.joelbgreenberg.db.IDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActionsInTransaction implements IDatabase {

    private final Map<String, Optional<String>> mappings = new MapMaker().makeMap();
    private final String transactionName;

    private static Logger LOG = LoggerFactory
            .getLogger(ActionsInTransaction.class);

    public ActionsInTransaction(String transactionName) {
        this.transactionName = transactionName;
    }

    @Override
    public void set(String name, String value) {
        LOG.info("{}: {} {} {}", this.transactionName, "SET", name, value);
        mappings.put(name, Optional.of(value));
    }

    @Override
    public Optional<String> get(String name) {
        if (!mappings.containsKey(name)) {
            LOG.info("{}: {} {}", this.transactionName, "GET", Optional.empty());
            return Optional.empty();
        }
        LOG.info("{}: {} {}", this.transactionName, "GET", mappings.get(name));
        return mappings.get(name);
    }

    @Override
    public void delete(String name) {
        LOG.info("{}: {} {}", this.transactionName, "DELETE", name);
        mappings.put(name, Optional.empty());
    }

    @Override
    public long count(String value) {
        LOG.info("{}: {} {} -> {}", this.transactionName, "COUNT", value, keys(value));
        return keys(value).size();
    }

    @Override
    public ImmutableSet<String> keys(String value) {
        return mappings.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isPresent())
                .filter(entry -> entry.getValue().get().equals(value))
                .map(Map.Entry::getKey)
                .collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ImmutableMap<String, Optional<String>> entryMap() {
        return ImmutableMap.copyOf(mappings);
    }

    @Override
    public void end() {

    }
}
