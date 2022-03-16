package com.joelbgreenberg.highnote.authrules.declined;

import com.joelbgreenberg.highnote.authrules.IProcessingRule;
import com.joelbgreenberg.highnote.dataelement.DataType;
import com.joelbgreenberg.highnote.dataelement.InputMessage;

public class ApproveIfSufficientBalance implements IProcessingRule {

    private final int currentBalanceCents;

    public ApproveIfSufficientBalance(int currentBalanceCents) {
        this.currentBalanceCents = currentBalanceCents;
    }

    @Override
    public boolean isRelevant(InputMessage message) {
        return true;
    }

    @Override
    public boolean testMessage(InputMessage message) {
        final int transactionAmountCents = Integer.parseInt(message.getData().get(DataType.TransactionAmountCents).getRawContent(), 10);
        return transactionAmountCents <= currentBalanceCents;
    }
}
