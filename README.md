# Transactions Database
An implementation of an in-memory data store with support for transactions.

## Start the Database

### Running with Docker

Assuming Docker is available locally, the project can be built with:

    % sh build.sh


Then, run:

    % sh run.sh

with sample output, e.g.:

```
% sh run.sh
>> GET a
NULL
>> SET a 1
>> COUNT 1
1
>> END
jbg@MacBook-Pro-3 transactions % sh run.sh
>> GET a
NULL
>> SET a foo
>> GET a
foo
>> SET b foo
>> COUNT foo
2
>> END
%
```

### Running with Gradle

Assuming dependencies are met, this application can be run with Gradle:

    ./gradlew bootRun --console=plain

A sample run might look like:

```
% ./gradlew bootRun --console=plain 
> Task :compileJava
> Task :processResources UP-TO-DATE
> Task :classes
> Task :bootRunMainClassName

> Task :bootRun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.6)

>> GET a
NULL
>> SET a foo
>> GET a
foo
>> SET b foo
>> COUNT foo
2
>> END

BUILD SUCCESSFUL in 38s
4 actionable tasks: 3 executed, 1 up-to-date

```

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

This is a Java 8 application that uses Gradle to manage tasks and dependencies.

## Testing

Automated tests can be run with:

    ./gradlew test
