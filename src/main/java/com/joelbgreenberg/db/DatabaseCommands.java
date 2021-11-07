package com.joelbgreenberg.db;

public enum DatabaseCommands {
    SET(2),
    GET(1),
    DELETE(1),
    COUNT(1),
    END(0),
    BEGIN(0),
    ROLLBACK(0),
    COMMIT(0);

    private final int commands;

    DatabaseCommands(int commands) {
        this.commands = commands;
    }

    public int getArgumentCount() {
        return commands;
    }
}
