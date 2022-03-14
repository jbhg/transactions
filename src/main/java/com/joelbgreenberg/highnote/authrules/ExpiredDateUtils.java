package com.joelbgreenberg.highnote.authrules;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ExpiredDateUtils {

    public static boolean isExpired(String mmddDate, YearMonth today) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMyy");
        final YearMonth expirationDate = YearMonth.parse(mmddDate, dateTimeFormatter);
        return !expirationDate.isAfter(today);
    }
}
