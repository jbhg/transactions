package com.joelbgreenberg.highnote.authrules;

import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class ExpiredDateUtilsTest {

    @Test
    void testExpiry() {
        assertFalse(ExpiredDateUtils.isExpired("1225", YearMonth.of(2020, 1)));
        assertFalse(ExpiredDateUtils.isExpired("1225", YearMonth.of(2025, 1)));
        assertFalse(ExpiredDateUtils.isExpired("1225", YearMonth.of(2025, 5)));
        assertFalse(ExpiredDateUtils.isExpired("1225", YearMonth.of(2025, 10)));
//        assertFalse(ExpiredDateUtils.isExpired("1225", YearMonth.of(2025, 12)));
        assertTrue(ExpiredDateUtils.isExpired("1225", YearMonth.of(2026, 1)));
        assertTrue(ExpiredDateUtils.isExpired("1225", YearMonth.of(2026, 10)));
    }
}
