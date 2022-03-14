package com.joelbgreenberg.highnote.authrules.declined;

import com.joelbgreenberg.highnote.authrules.IProcessingRule;
import com.joelbgreenberg.highnote.dataelement.DataType;
import com.joelbgreenberg.highnote.dataelement.InputMessage;
import com.joelbgreenberg.highnote.fieldprocessor.HighnoteFieldProcessResult;

import java.util.Objects;

public class ApproveIfZipAbsentAndAmountLessThan100 implements IProcessingRule {

    @Override
    public boolean isRelevant(InputMessage message) {
        return !message.getData().containsKey(DataType.ZipCode); // ZIP code is present
    }

    @Override
    public boolean testMessage(InputMessage message) {
        final HighnoteFieldProcessResult<Integer> result = (HighnoteFieldProcessResult<Integer>) message.getData().get(DataType.TransactionAmountCents);
        return Objects.requireNonNull(result.getParsedContent()) < 100_00;
    }
}
