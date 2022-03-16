package com.joelbgreenberg.highnote;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.joelbgreenberg.highnote.authrules.IProcessingRule;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfNotExpired;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfSufficientBalance;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfZipAbsentAndAmountLessThan100;
import com.joelbgreenberg.highnote.authrules.declined.ApproveIfZipProvidedAndAmountLessThan200;
import com.joelbgreenberg.highnote.dataelement.DataType;
import com.joelbgreenberg.highnote.dataelement.InputMessage;
import com.joelbgreenberg.highnote.fieldprocessor.DataTypeAlpha;
import com.joelbgreenberg.highnote.fieldprocessor.DataTypeLLVAR;
import com.joelbgreenberg.highnote.fieldprocessor.DataTypeNumeric;
import com.joelbgreenberg.highnote.fieldprocessor.IHighnoteFieldProcessor;

import java.time.YearMonth;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public class Configuration {
    public static final ImmutableSet<DataType> REQUIRED_FIELDS = ImmutableSet.of(
            DataType.PAN,
            DataType.ExpirationDate,
            DataType.TransactionAmountCents
    );

    public static final BiFunction<YearMonth, Integer, ImmutableSet<IProcessingRule>> BUSINESS_RULES_TO_TEST = (expiry, currentBalanceCents) -> ImmutableSet.of(
            new ApproveIfSufficientBalance(currentBalanceCents),
            new ApproveIfNotExpired(expiry),
            new ApproveIfZipProvidedAndAmountLessThan200(),
            new ApproveIfZipAbsentAndAmountLessThan100()
    );

    public static final ImmutableList<Integer> ORDERED_INDEXES = ImmutableList.of(1, 2, 3, 4, 5, 6, 7, 8);

    public static final ImmutableList<String> SAMPLE_INPUT = ImmutableList.of(
            "0100e016411111111111111112250000001000",
            "0100e016401288888888188112250000011000",
            "0100ec1651051051051051001225000001100011MASTER YODA90089",
            "0100e016411111111111111112180000001000",
            "01006012250000001000"
    );

    public static final ImmutableMap<DataType, IHighnoteFieldProcessor<?>> DATA_TYPE_STRATEGY_MAP = ImmutableMap.of(
            DataType.PAN, DataTypeLLVAR.withLengthOfNumericComponent(2),
            DataType.ExpirationDate, DataTypeNumeric.withNumberLength(4),
            DataType.TransactionAmountCents, DataTypeNumeric.withNumberLength(10),
            DataType.ResponseCode, DataTypeAlpha.withAlphaLength(2),
            DataType.CardholderName, DataTypeLLVAR.withLengthOfNumericComponent(2),
            DataType.ZipCode, DataTypeNumeric.withNumberLength(5)
    );
}
