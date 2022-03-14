package com.joelbgreenberg.highnote.dataelement;

import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

public enum MessageType {
    AUTH_REQUEST("0100"),
    AUTH_RESPONSE("0110");

    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Optional<MessageType> parseCode(String code) {
        return EnumUtils
                .getEnumList(MessageType.class)
                .stream()
                .filter(m -> m.getCode().equals(code))
                .findFirst();
    }
}
