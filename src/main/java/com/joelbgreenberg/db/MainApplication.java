package com.joelbgreenberg.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.joelbgreenberg.charthop.Change;
import com.joelbgreenberg.charthop.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@SpringBootApplication
@Component
public class MainApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, ParseException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		List<Person> people = objectMapper.readValue(new URL("file:src/main/resources/persons.json"), new TypeReference<List<Person>>() {
		});

		List<Change> change = objectMapper.readValue(new URL("file:src/main/resources/changes.json"), new TypeReference<List<Change>>() {
		});

		System.out.println("Found " + people.size() + " people and " + change.size() + " changes.");

		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

		/// QUESTION 1
		final String question1JobId = "5a13d80dcfed7957fe6c04a5";
		final Date question1Date = formatter.parse("05-05-2019");
		change.stream()
				.filter(c -> c.getJobId().equals(question1JobId)) // find only matching job id
				.filter(c -> c.getDate().before(question1Date))// find only entries before May 5, 2019
				.filter(c -> c.getData() != null
						&& c.getData().getComp() != null
						&& c.getData().getComp().getBase() > 0
						&& c.getData().getComp().getCurrency() != null)// find only entries specifying salary information
				.max(Comparator.comparing(Change::getDate))
				.ifPresent(question1Changes -> System.out.println(String.format("Question 1: Job={%s}, Date={%s}: latest salary is {%s} {%d}.",
						question1Changes.getJobId(),
						question1Changes.getDate(),
						question1Changes.getData().getComp().getCurrency(),
						question1Changes.getData().getComp().getBase()
				)));

		/// QUESTION 2
		final String first = "Samson";
		final String last = "Oren";
		final Date asOfDate = formatter.parse("30-04-2019");
		people.stream()
				.filter(p -> p.getName() != null)
				.filter(p -> p.getName().getFirst().equals(first) && p.getName().getLast().equals(last))
				.findFirst()
				.ifPresent(samsonOrenPerson -> change.stream()
						.filter(c -> c.getData() != null
//								&& c.getData().getTitle() != null
								&& c.getData().getPersonId() != null
								&& c.getData().getPersonId().equals(samsonOrenPerson.get_id()))
						.filter(c -> c.getDate().before(asOfDate))
						.max(Comparator.comparing(Change::getDate))
						.filter(c -> {
							System.out.println("Found " + first);
							return true;
						})
						.ifPresent(question2Job -> {
							System.out.println(String.format(
									"Question 2: {%s} {%s}'s job as of {%s} was {%s}.",
									samsonOrenPerson.getName().getFirst(),
									samsonOrenPerson.getName().getLast(),
									question2Job.getDate(),
									question2Job.getJobId()
							));
							change.stream()
									.filter(c -> c.getJobId().equals(question2Job.getJobId()))


						}));
	}
}
