package com.joelbgreenberg.app;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Component
public class MainApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		final String input = "{ // comment here\n" +
				"  \"key1\": \"val\\\"ue1\", // hey cool\n" +
				"  \"urls\": [\"https://humaninterest.com\"]\n" +
				"}";
		final ImmutableList<String> splitForProcessing = ImmutableList.copyOf(Splitter.on('\n').split(input));
		final List<String> filteredForComments = splitForProcessing
				.stream()
				.peek(i -> {
					System.out.println("Line: " + i);
				})
				.map(MainApplication::removeCommentFromString)
				.peek(i -> {
					System.out.println("\tResult: " + i);
				})
				.collect(Collectors.toList());
		System.out.println(Joiner.on("\n").join(filteredForComments));


	}


	public static String removeCommentFromString(String input) {
		if (input == null || input.length() == 0) {
			return "";
		}
		// Split on \" when it is not also within \\\"
		final ImmutableList<String> splitByQuotes = ImmutableList.copyOf(input.split("\""));
		final String lastTokenRaw = splitByQuotes.get(splitByQuotes.size() - 1);
		final String lastToken =
				(lastTokenRaw.contains("//")) ?
						lastTokenRaw.substring(0, input.indexOf("//")) :
						lastTokenRaw;
		final ImmutableList<String> result = ImmutableList.<String>builder()
				.addAll(splitByQuotes.subList(0, Math.max(0, splitByQuotes.size() - 1)))
				.add(lastToken)
				.build();
		return Joiner.on("\"").join(result);
	}


	{
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
//		List<Person> people = objectMapper.readValue(new URL("file:src/main/resources/persons.json"), new TypeReference<List<Person>>() {
//		});
//
//		List<Change> change = objectMapper.readValue(new URL("file:src/main/resources/changes.json"), new TypeReference<List<Change>>() {
//		});
//
//		System.out.println("Found " + people.size() + " people and " + change.size() + " changes.");
//
//		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

	}
}
