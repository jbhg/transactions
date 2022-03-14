package com.joelbgreenberg.highnote.authrules;

import com.joelbgreenberg.highnote.dataelement.InputMessage;

public interface IProcessingRule {

    boolean isRelevant(InputMessage message);

    boolean testMessage(InputMessage message);

}
