package com.joelbgreenberg.db.inmemorydb;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;
import com.joelbgreenberg.db.IDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class ActionsInTransaction implements IDatabase {

    // Key-Value Mappings. Value is an Optional to be able to capture deletions.
    private final Map<String, Optional<String>> storedKeyValueMapping = new MapMaker().makeMap();

    // Value-Key Mappings for fast lookup of all keys matching a given value.
    private final Multimap<String, String> reverseValueKeyLookupMultiMap = HashMultimap.create();

    // The name of this "transaction" for debugging.
    private final String transactionName;

    private static Logger LOG = LoggerFactory
            .getLogger(ActionsInTransaction.class);

    public ActionsInTransaction(String transactionName) {
        this.transactionName = transactionName;
    }

    @Override
    public void set(String name, String value) {
        LOG.info("{}: {} {} {}", this.transactionName, "SET", name, value);
        this.get(name).ifPresent(v -> reverseValueKeyLookupMultiMap.remove(v, name));
        storedKeyValueMapping.put(name, Optional.of(value));
        reverseValueKeyLookupMultiMap.put(value, name);
    }

    @Override
    public Optional<String> get(String name) {
        if (!storedKeyValueMapping.containsKey(name)) {
            LOG.info("{}: {} {}", this.transactionName, "GET", Optional.empty());
            return Optional.empty();
        }
        LOG.info("{}: {} {}", this.transactionName, "GET", storedKeyValueMapping.get(name));
        return storedKeyValueMapping.get(name);
    }

    @Override
    public void delete(String name) {
        LOG.info("{}: {} {}", this.transactionName, "DELETE", name);
        this.get(name).ifPresent(v -> reverseValueKeyLookupMultiMap.remove(v, name));
        storedKeyValueMapping.put(name, Optional.empty());
    }

    @Override
    public long count(String value) {
        LOG.info("{}: {} {} -> {}", this.transactionName, "COUNT", value, keys(value));
        return 0;
    }

    @Override
    public ImmutableSet<String> keys(String value) {
        return ImmutableSet.copyOf(reverseValueKeyLookupMultiMap.get(value));
    }

    @Override
    public ImmutableMap<String, Optional<String>> entryMap() {
        return ImmutableMap.copyOf(storedKeyValueMapping);
    }

    @Override
    public void end() {

    }
}
