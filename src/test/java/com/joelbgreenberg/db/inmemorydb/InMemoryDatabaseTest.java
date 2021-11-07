package com.joelbgreenberg.db.inmemorydb;

import com.joelbgreenberg.db.ITransactionalDatabase;
import org.junit.jupiter.api.Test;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;

public class InMemoryDatabaseTest {

    @Test
    public void testStackedTransactionsWithRollbackThenCommit() {
        // Arrange
        ITransactionalDatabase db = new InMemoryDatabase();

        db.set("a", "foo");
        db.set("b", "baz");

        // Act
        db.beginTransaction();
        db.beginTransaction();
        db.beginTransaction();
        db.set("a", "JUNK");
        db.set("b", "JUNK");
        assertThat(db.get("a"), isPresentAndIs("JUNK"));
        assertThat(db.get("b"), isPresentAndIs("JUNK"));

        db.beginTransaction();
        db.set("a", "MORE JUNK");
        db.set("b", "MORE JUNK");
        assertThat(db.get("a"), isPresentAndIs("MORE JUNK"));
        assertThat(db.get("b"), isPresentAndIs("MORE JUNK"));

        db.rollbackTransaction();
        db.beginTransaction();
        db.beginTransaction();
        db.beginTransaction();
        db.commit();

        // Assert
        assertThat(db.get("a"), isPresentAndIs("JUNK"));
        assertThat(db.get("b"), isPresentAndIs("JUNK"));
    }

    @Test
    public void testConflictingStackedTransactionsWithCommit() {
        // Arrange
        ITransactionalDatabase db = new InMemoryDatabase();

        db.set("a", "Apple");
        db.set("b", "Banana");
        db.set("c", "Chair");
        db.set("d", "Dolphin");

        // Act
        db.beginTransaction();
        db.delete("a");
        db.set("b", "Boar");

        db.beginTransaction();
        db.set("a", "Astronaut");
        db.delete("c");

        db.beginTransaction();
        db.set("c", "Chaos");

        db.beginTransaction();
        db.delete("c");

        // Act
        db.commit();

        // Assert
        assertThat(db.get("a"), isPresentAndIs("Astronaut"));
        assertThat(db.get("b"), isPresentAndIs("Boar"));
        assertThat(db.get("c"), isEmpty());
        assertThat(db.get("d"), isPresentAndIs("Dolphin"));
    }


    @Test
    public void testRollbackOfDeletion() {
        // Arrange
        ITransactionalDatabase db = new InMemoryDatabase();
        db.set("a", "Apple");
        db.set("b", "Banana");
        db.set("c", "Chair");
        db.set("d", "Dolphin");

        // Delete all of these
        db.beginTransaction();
        db.delete("a");
        db.delete("b");
        db.delete("c");
        db.delete("d");
        db.rollbackTransaction();

        // Assert
        assertThat(db.get("a"), isPresentAndIs("Apple"));
        assertThat(db.get("b"), isPresentAndIs("Banana"));
        assertThat(db.get("c"), isPresentAndIs("Chair"));
        assertThat(db.get("d"), isPresentAndIs("Dolphin"));
    }
}
