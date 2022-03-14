package com.joelbgreenberg.highnote.dataelement;

public enum ResponseCode {
    Authorized("OK"),
    Declined("DE"),
    MandatoryFieldsMissing("ER");

    public String getCode() {
        return code;
    }

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }
}
