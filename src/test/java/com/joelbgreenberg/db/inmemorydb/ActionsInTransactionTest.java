package com.joelbgreenberg.db.inmemorydb;

import com.joelbgreenberg.db.ITransactionalDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ActionsInTransactionTest {

    @Test
    public void testActionsInTransaction() {
        ActionsInTransaction ait = new ActionsInTransaction();
        assertThat(ait.count("foo"), is(0L));

        assertThat(ait.get("a"), isEmpty());

        ait.set("a", "foo");
        ait.set("b", "foo");

        assertThat(ait.count("foo"), is(2L));

        ait.delete("b");
        ait.delete("c");

        assertThat(ait.count("foo"), is(1L));
    }
}
