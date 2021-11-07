package com.joelbgreenberg.db.inmemorydb;

import com.joelbgreenberg.db.IDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class InMemoryDatabaseTestsFromExamples {

	@Test
	public void testExample1() {
		IDatabase db = new InMemoryDatabase();
		assertThat(db.get("a"), is(Optional.empty()));
		db.set("a", "foo");
		db.set("b", "foo");
		assertThat(db.count("foo"), is(2));
		assertThat(db.count("bar"), is(0));
		db.delete("a");
		assertThat(db.count("foo"), is(1));
		db.set("b", "baz");
		assertThat(db.count("foo"), is(0));
		assertThat(db.get("b"), isPresentAndIs("baz"));
		assertThat(db.get("B"), isEmpty());
		db.end();
	}

	@Test
	public void testExample2() {
		IDatabase db = new InMemoryDatabase();
		db.set("a", "foo");
		db.set("a", "foo");
		assertThat(db.count("foo"), is(1));
		assertThat(db.get("a"), isPresentAndIs("foo"));
		db.delete("a");
		assertThat(db.get("a"), isEmpty());
		assertThat(db.count("foo"), is(0));
		db.end();
	}

	@Test
	public void testExample3() {
		IDatabase db = new InMemoryDatabase();
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
		IDatabase db = new InMemoryDatabase();

		db.set("a", "foo");
		db.set("b", "baz");

		db.beginTransaction();
		assertThat(db.get("a"), isPresentAndIs("foo"));
		db.set("a", "bar");
		assertThat(db.count("bar"), is(1));

		db.beginTransaction();
		assertThat(db.count("bar"), is(1));
		db.delete("a");
		assertThat(db.get("a"), isEmpty());
		assertThat(db.count("bar"), is(0));

		db.rollbackTransaction();
		assertThat(db.get("a"), isPresentAndIs("bar"));
		assertThat(db.count("bar"), is(1));

		db.commit();
		assertThat(db.get("a"), isPresentAndIs("bar"));
		assertThat(db.get("b"), isPresentAndIs("baz"));

		db.end();
	}
}
