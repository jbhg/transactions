package com.joelbgreenberg.highnote.authrules.declined;

import com.joelbgreenberg.highnote.authrules.ExpiredDateUtils;
import com.joelbgreenberg.highnote.authrules.IProcessingRule;
import com.joelbgreenberg.highnote.dataelement.DataType;
import com.joelbgreenberg.highnote.dataelement.InputMessage;

import java.time.YearMonth;
import java.util.Objects;

public class ApproveIfNotExpired implements IProcessingRule {

    private final YearMonth currentYearAndMonth;

    public ApproveIfNotExpired(YearMonth currentYearAndMonth) {
        this.currentYearAndMonth = currentYearAndMonth;
    }

    @Override
    public boolean isRelevant(InputMessage message) {
        return true; // REQUIRED
    }

    @Override
    public boolean testMessage(InputMessage message) {
        final String dateString = Objects.requireNonNull(message.getData().get(DataType.ExpirationDate)).getRawContent();
        final boolean isExpired = ExpiredDateUtils.isExpired(dateString, this.currentYearAndMonth);
        return !isExpired;
    }

    @Override
    public String toString() {
        return "ApproveIfNotExpired{" +
                "currentYearAndMonth=" + currentYearAndMonth +
                '}';
    }
}
