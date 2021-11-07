package com.joelbgreenberg.db.inmemorydb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ActionsInTransactionTest {

    @Test
    public void testActionsInTransaction() {

        // Arrange
        ActionsInTransaction ait = new ActionsInTransaction("TRANSACTION");
        assertThat(ait.count("foo"), is(0L));
        assertThat(ait.get("a"), isEmpty());

        // Act
        ait.set("a", "foo");
        ait.set("b", "foo");

        // Assert
        assertThat(ait.count("foo"), is(2L));

        // Act (2)
        ait.delete("b");
        ait.delete("c");

        // Assert (2)
        assertThat(ait.count("foo"), is(1L));
    }
}
