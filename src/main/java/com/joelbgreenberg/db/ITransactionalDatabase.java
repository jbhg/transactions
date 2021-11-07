package com.joelbgreenberg.db;

import java.util.Optional;

public interface ITransactionalDatabase extends IDatabase {

    /**
     * Begins a new transaction.
     */
    void beginTransaction();

    /**
     * Rolls back the most recent transaction.
     *
     * If there is no transaction to rollback, prints TRANSACTION NOT FOUND.
     *
     * (In the TRANSACTION NOT FOUND case, the result will be handled with an empty Optional.)
     */
    Optional<Integer> rollbackTransaction();

    /**
     * Commits all of the open transactions.
     */
    void commit();
}
