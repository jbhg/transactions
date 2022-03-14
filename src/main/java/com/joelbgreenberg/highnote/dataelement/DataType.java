package com.joelbgreenberg.highnote.dataelement;

import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

public enum DataType {

    PAN(1, DataTypeIsRequired.Required, DataTypeIsRequired.Required),
    ExpirationDate(2, DataTypeIsRequired.Required, DataTypeIsRequired.Required),
    TransactionAmountCents(3, DataTypeIsRequired.Required, DataTypeIsRequired.Required),
    ResponseCode(4, DataTypeIsRequired.NotRequired, DataTypeIsRequired.Required),
    CardholderName(5, DataTypeIsRequired.NotRequired, DataTypeIsRequired.NotRequired),
    ZipCode(6, DataTypeIsRequired.NotRequired, DataTypeIsRequired.NotRequired);

    private final int bit;
    private final DataTypeIsRequired requiredForRequest;
    private final DataTypeIsRequired requiredForResponse;

    DataType(int bit, DataTypeIsRequired requiredForRequest, DataTypeIsRequired requiredForResponse) {
        this.bit = bit;
        this.requiredForRequest = requiredForRequest;
        this.requiredForResponse = requiredForResponse;
    }

    public static Optional<DataType> fromBit(int bit) {
        return EnumUtils
                .getEnumList(DataType.class)
                .stream()
//                .peek(n -> System.out.println("Lookup of bit " + bit))
                .filter(m -> m.getBit() == bit)
                .findFirst();
    }

    public int getBit() {
        return bit;
    }

    public DataTypeIsRequired getRequiredForRequest() {
        return requiredForRequest;
    }

    public DataTypeIsRequired getRequiredForResponse() {
        return requiredForResponse;
    }
}
