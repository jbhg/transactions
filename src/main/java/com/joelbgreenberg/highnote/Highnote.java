package com.joelbgreenberg.highnote;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.joelbgreenberg.highnote.authrules.IProcessingRule;
import com.joelbgreenberg.highnote.dataelement.DataType;
import com.joelbgreenberg.highnote.dataelement.InputMessage;
import com.joelbgreenberg.highnote.dataelement.MessageType;
import com.joelbgreenberg.highnote.dataelement.ResponseCode;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public class Highnote {

    private final YearMonth expiry;
    private final Map<String, AtomicInteger>  accountBalances;

    public int getAccountBalancesSize() {
        return accountBalances.size();
    }

    public int getPanBalance(String pan) {
        if (accountBalances.containsKey(pan)) {
            return accountBalances.get(pan).get();
        } else {
            return -1;
        }
    }

    public Highnote(YearMonth expiry) {
        this.expiry = expiry;
        this.accountBalances = new HashMap<>();
    }

    public String processTransaction(String input) {
        System.out.println(" == PROCESS SINGLE TRANSACTION: " + input + " == ");
        final InputMessage message = InputMessage.createFromString(input);

       if (!message.getData().keySet().containsAll(Configuration.REQUIRED_FIELDS)) {
            System.out.println("ER response: a required field was missing: " + Sets.difference(Configuration.REQUIRED_FIELDS, message.getData().keySet()));
            final String result =  InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.MandatoryFieldsMissing).serializeToString();
            System.out.println(" == PROCESS SINGLE TRANSACTION COMPLETE: " + result + " == ");
            return result;
        }

        final String pan = message.getData().get(DataType.PAN).getRawContent();
        if (!accountBalances.containsKey(pan)) {
            accountBalances.put(pan, new AtomicInteger(300_00));
        }

        final int transactionAmountCents = Integer.parseInt(message.getData().get(DataType.TransactionAmountCents).getRawContent(), 10);
        final int currentBalanceCents =  accountBalances.get(pan).get();

        final ImmutableSet<IProcessingRule> rulesViolations = Configuration.BUSINESS_RULES_TO_TEST
                .apply(expiry,currentBalanceCents) // specified as a function
                .stream()
                .filter(r -> r.isRelevant(message)) // don't apply rules we know to be irrelevant.
                .filter(r -> !r.testMessage(message)) // check for violations
                .collect(collectingAndThen(toSet(), ImmutableSet::copyOf));


        if (!rulesViolations.isEmpty())
        {
            System.out.println("DE response since at least one condition was violated was missing: " + rulesViolations);
            final String result = InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.Declined).serializeToString();
            System.out.println(" == PROCESS SINGLE TRANSACTION COMPLETE: " + result + " == ");
            return result;
        }
        // else if transaction would overdraw account
//        else if (transactionAmountCents >currentBalanceCents) {
//            final String result = InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.Declined).serializeToString();
//            System.out.println(" == PROCESS SINGLE TRANSACTION COMPLETE: " + result + " == ");
//            return result;
//        }
        else
        {
            accountBalances
                    .get(pan)
                    .set(currentBalanceCents - transactionAmountCents);
            final String result = InputMessage.createFromMessageWithAuthCode(message, MessageType.AUTH_RESPONSE, ResponseCode.Authorized).serializeToString();
            System.out.println(" == PROCESS SINGLE TRANSACTION COMPLETE: " + result + " == ");
            return result;
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
