package com.joelbgreenberg.db.inmemorydb;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapMaker;
import com.joelbgreenberg.db.IDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
        LOG.info("{}: {} {} {}", this.transactionName, "set".toUpperCase(Locale.ROOT), name, value);
        mappings.put(name, Optional.of(value));
    }

    @Override
    public Optional<String> get(String name) {
        LOG.info("{}: {} {}", this.transactionName, "get".toUpperCase(Locale.ROOT), name);
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
    public ImmutableMap<String, Optional<String>> entryMap() {
        return ImmutableMap.copyOf(mappings);
    }

    @Override
    public void end() {

    }
}
