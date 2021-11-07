package com.joelbgreenberg.db.inmemorydb;

import com.google.common.collect.ImmutableList;
import com.joelbgreenberg.db.IDatabase;
import com.joelbgreenberg.db.ITransactionalDatabase;
import org.junit.jupiter.api.Test;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class InMemoryDatabaseTestsFromExamples {

	@Test
	public void testExample1() {
		for (IDatabase db : ImmutableList.of(new InMemoryDatabase(), new ActionsInTransaction("TX"))) {
			assertThat(db.get("a"), isEmpty());
			db.set("a", "foo");
			db.set("b", "foo");
			assertThat(db.count("foo"), is(2L));
			assertThat(db.count("bar"), is(0L));
			db.delete("a");
			assertThat(db.count("foo"), is(1L));
			db.set("b", "baz");
			assertThat(db.count("foo"), is(0L));
			assertThat(db.get("b"), isPresentAndIs("baz"));
			assertThat(db.get("B"), isEmpty());
			db.end();
		}
	}

	@Test
	public void testExample2() {
		for (IDatabase db : ImmutableList.of(new InMemoryDatabase(), new ActionsInTransaction("TX"))) {

			db.set("a", "foo");
			db.set("a", "foo");
			assertThat(db.count("foo"), is(1L));
			assertThat(db.get("a"), isPresentAndIs("foo"));
			db.delete("a");
			assertThat(db.get("a"), isEmpty());
			assertThat(db.count("foo"), is(0L));
			db.end();
		}
	}

	@Test
	public void testExample3() {
		ITransactionalDatabase db = new InMemoryDatabase();
		db.beginTransaction();
		db.set("a", "foo");
		assertThat(db.get("a"), isPresentAndIs("foo"));

		db.beginTransaction();
		db.set("a", "bar");
		assertThat(db.get("a"), isPresentAndIs("bar"));
		db.set("a", "baz");

		db.rollbackTransaction();
		assertThat(db.get("a"), isPresentAndIs("foo"));

		db.rollbackTransaction();
		assertThat(db.get("a"), isEmpty());

		db.end();
	}

	@Test
	public void testExample4() {
		ITransactionalDatabase db = new InMemoryDatabase();

		db.set("a", "foo");
		db.set("b", "baz");

		db.beginTransaction();
		assertThat(db.get("a"), isPresentAndIs("foo"));
		db.set("a", "bar");
		assertThat(db.count("bar"), is(1L));

		db.beginTransaction();
		assertThat(db.count("bar"), is(1L));
		db.delete("a");
		assertThat(db.get("a"), isEmpty());
		assertThat(db.count("bar"), is(0L));

		db.rollbackTransaction();
		assertThat(db.get("a"), isPresentAndIs("bar"));
		assertThat(db.count("bar"), is(1L));

		db.commit();
		assertThat(db.get("a"), isPresentAndIs("bar"));
		assertThat(db.get("b"), isPresentAndIs("baz"));

		db.end();
	}
}
