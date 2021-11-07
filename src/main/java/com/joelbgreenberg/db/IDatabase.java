package com.joelbgreenberg.db;

import java.util.Optional;

/**
 * The points in this section are goals for the performance of the solution.
 * ● Aim for GET, SET, DELETE, and COUNT to all have a runtime of less than O(log n), if not better
 * (where n is the number of items in the database).
 * ● The memory usage of the database shouldn't be doubled for every transaction.
 */
public interface IDatabase {

    /**
     * Sets the name in the database to the given value.
     * @param name
     * @param value
     */
    void set(String name, String value);

    /**
     * Prints the value for the given name.
     * If the value is not in the database, prints NULL.
     *
     * (An empty value will be handled with an empty Optional.)
     *
     * @param name
     * @return
     */
    Optional<String> get(String name);

    /**
     * Deletes the value from the database
     * @param name
     */
    void delete(String name);

    /**
     * Returns the number of names that have the given value assigned to them.
     *
     * If that value is not assigned anywhere, prints 0
     *
     * @return
     */
    long count(String value);

    /**
     * Exits the database.
     */
    void end();
}
