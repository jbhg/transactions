package com.joelbgreenberg.highnote;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfNotExpired;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfZipAbsentAndAmountLessThan100;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfZipProvidedAndAmountLessThan200;
import com.joelbgreenberg.highnote.authrules.IProcessingRule;
import com.joelbgreenberg.highnote.dataelement.ResponseCode;
import com.joelbgreenberg.highnote.dataelement.InputMessage;
import com.joelbgreenberg.highnote.dataelement.MessageType;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Highnote {

    private final YearMonth expiry;

    public Highnote(YearMonth expiry) {
        this.expiry = expiry;
    }

    public String processTransaction(String input) {
        final InputMessage message = InputMessage.createFromString(input);

        if (!message.getData().keySet().containsAll(Configuration.REQUIRED_FIELDS)) {
            System.out.println("ER response: a required field was missing: " + Sets.difference(Configuration.REQUIRED_FIELDS, message.getData().keySet()));
            return InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.MandatoryFieldsMissing).serializeToString();
        }

        final Set<IProcessingRule> rulesViolations = Configuration.BUSINESS_RULES_TO_TEST
                .apply(expiry) // specified as a function
                .stream()
                .filter(r -> r.isRelevant(message))
                .filter(r -> !r.testMessage(message))
                .collect(Collectors.toSet());
        if (rulesViolations.isEmpty()) {
            return InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.Authorized).serializeToString();
        } else {
            System.out.println("DE response since at least one condition was violated was missing: " + rulesViolations);
            return InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.Declined).serializeToString();
        }
    }

    public String[] processTransactions(String[] input) {
        return Arrays.stream(input)
                .peek(message -> System.out.println(String.format("PROCESSING message: %s", message)))
                .map(this::processTransaction)
                .peek(message -> System.out.println(String.format("COMPLETE -> PROCESSING message: %s", message)))
                .peek(message -> System.out.println("=============================="))
                .toArray(String[]::new);
    }
}
