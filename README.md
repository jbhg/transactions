# Transactions Database
An implementation of an in-memory data store with support for transactions.

## Commands

SETs the name in the database to the given value.

    SET [name] [value]

GETs and prints the value for the given name. If the value is not in the database, prints NULL.

    GET [name]

DELETEs the value from the database.

    DELETE [name]

Returns the number of `name`s that have the given `value` assigned to them. If that `value` is not assigned anywhere, prints 0.

    COUNT [value]

Exits the database.

    END

Begins a new transaction.

    BEGIN

Rolls back the most recent transaction. If there is no transaction to rollback, prints `TRANSACTION NOT FOUND`.

    ROLLBACK

Commits *all of the* open transactions.

    COMMIT

## Dependencies
