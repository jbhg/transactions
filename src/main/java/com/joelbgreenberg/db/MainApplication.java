package com.joelbgreenberg.db;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.joelbgreenberg.db.inmemorydb.InMemoryDatabase;
import com.joelbgreenberg.db.runner.DatabaseCommandMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
@Component
public class MainApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ITransactionalDatabase db = new InMemoryDatabase();
		AtomicBoolean isHalted = new AtomicBoolean(false);
		DatabaseCommandMapper commandMapper = new DatabaseCommandMapper(db);
		Scanner scanner = new Scanner(System.in);
		while (!isHalted.get()) {
			System.out.print(">> ");
			final String line = scanner.nextLine().trim();
			if (line.length() == 0) {
				continue;
			}
			final ImmutableList<String> parsedUserInput = ImmutableList.copyOf(Splitter.on(" ").split(line.trim()));
			commandMapper.doCommand(parsedUserInput);
		}
	}
}
