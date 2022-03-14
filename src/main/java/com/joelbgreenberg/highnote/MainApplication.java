package com.joelbgreenberg.highnote;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@SpringBootApplication
@Component
public class MainApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Highnote highnote = new Highnote(YearMonth.of(2022,3));
		String[] result = highnote.processTransactions(Configuration.SAMPLE_INPUT.toArray(new String[0]));

	}
}
