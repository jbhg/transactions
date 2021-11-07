package com.joelbgreenberg.db.runner;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.joelbgreenberg.db.DatabaseCommands;
import com.joelbgreenberg.db.ITransactionalDatabase;

public class DatabaseCommandMapper {

    private final ITransactionalDatabase db;

    public DatabaseCommandMapper(ITransactionalDatabase db) {
        this.db = db;
    }

    public void doCommand(ImmutableList<String> parsedUserInput) {
        final Optional<DatabaseCommands> parsedDatabaseCommand = Enums.getIfPresent(DatabaseCommands.class, parsedUserInput.get(0));
        if (!parsedDatabaseCommand.isPresent()) {
            System.err.println("Database command not recognized: " + parsedUserInput.get(0));
            return;
        }

        final DatabaseCommands dbCommand = parsedDatabaseCommand.get();
        if (dbCommand.getArgumentCount() != parsedUserInput.size() - 1) {
            System.err.println("Database command " + dbCommand + " expected " + dbCommand.getArgumentCount() + " arguments; instead found " + (parsedUserInput.size() - 1) + ".");
            return;
        }

        switch (DatabaseCommands.valueOf(parsedUserInput.get(0))) {
            case SET:
                db.set(parsedUserInput.get(1), parsedUserInput.get(2));
                break;
            case GET:
                System.out.println(db.get(parsedUserInput.get(1)).orElse("NULL"));
                break;
            case DELETE:
                db.delete(parsedUserInput.get(1));
                break;
            case COUNT:
                System.out.println(db.count(parsedUserInput.get(1)));
                break;
            case END:
                db.end();
                System.exit(0);
                break;
            case BEGIN:
                db.beginTransaction();
                break;
            case ROLLBACK:
                if (!db.rollbackTransaction().isPresent()) {
                    System.out.println("TRANSACTION NOT FOUND");
                }
                break;
            case COMMIT:
                db.commit();
                break;
            default:
                System.err.println("Unknown matching enumerated type (not yet implemented): " + parsedUserInput.get(0));
        }
    }
}
