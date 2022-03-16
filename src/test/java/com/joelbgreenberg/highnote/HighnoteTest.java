package com.joelbgreenberg.highnote;

import com.google.common.collect.ImmutableList;
import com.joelbgreenberg.highnote.dataelement.InputMessage;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HighnoteTest {


    @Test
    void processApprovedTransactionOf1000() {
        final ImmutableList<String> input = ImmutableList.of(
                "0100e016411111111111111112250000001000"
        );

        final Highnote highnote = new Highnote(YearMonth.of(2022,3));
        final String result = highnote.processTransaction(input.get(0));
        assertEquals("0110f016411111111111111112250000001000OK", result);

    }

    @Test
    void processApprovedTransactionOf10002() {
        final ImmutableList<String> input = ImmutableList.of(
                "0100e016401288888888188112250000011000"
        );
        final Highnote highnote = new Highnote(YearMonth.of(2022,3));
        final String result = highnote.processTransaction(input.get(0));
        assertEquals("0110f016401288888888188112250000011000DE", result);
    }



    @Test
    void processApprovedTransactionOf10003() {
        final ImmutableList<String> input = ImmutableList.of(
                "0100ec1651051051051051001225000001100011MASTER YODA90089"
        );
        final Highnote highnote = new Highnote(YearMonth.of(2022,3));
        final String result = highnote.processTransaction(input.get(0));
        assertEquals("0110fc16510510510510510012250000011000OK11MASTER YODA90089", result);
    }

    @Test
    void testFailureOf110DollarTransactionRunThreeTimes() {
        final ImmutableList<String> input = ImmutableList.of(
                "0100ec1651051051051051001225000001100011MASTER YODA90089",
                "0100ec1651051051051051001225000001100011MASTER YODA90089",
                "0100ec1651051051051051001225000001100011MASTER YODA90089"
        );
        final Highnote highnote = new Highnote(YearMonth.of(2022,3));
        final String[] result = highnote.processTransactions(input.toArray(new String[0]));
        assertEquals(300_00 - 11_000 - 11_000, highnote.getPanBalance("165105105105105100"));
        assertEquals("0110fc16510510510510510012250000011000OK11MASTER YODA90089", result[0]);
        assertEquals("0110fc16510510510510510012250000011000OK11MASTER YODA90089", result[1]);
        assertEquals("0110fc16510510510510510012250000011000DE11MASTER YODA90089", result[2]);
    }

    @Test
    void processApprovedTransactionOf10004() {
        final ImmutableList<String> input = ImmutableList.of(
                "0100e016411111111111111112180000001000"
        );
        final Highnote highnote = new Highnote(YearMonth.of(2022,3));
        final String result = highnote.processTransaction(input.get(0));
        assertEquals("0110f016411111111111111112180000001000DE", result);
        assertEquals(300_00, highnote.getPanBalance("164111111111111111"));
    }

    @Test
    void processApprovedTransactionOf10005() {
        final ImmutableList<String> input = ImmutableList.of(
                "01006012250000001000"
        );
        final Highnote highnote = new Highnote(YearMonth.of(2022,3));
        final String result = highnote.processTransaction(input.get(0));
        assertEquals("01107012250000001000ER", result);
        assertEquals(0, highnote.getAccountBalancesSize());
    }
}