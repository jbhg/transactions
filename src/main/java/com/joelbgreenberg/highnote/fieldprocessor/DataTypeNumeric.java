package com.joelbgreenberg.highnote.fieldprocessor;

public class DataTypeNumeric implements IHighnoteFieldProcessor<Integer>{

    private static final int BASE_TEN_RADIX = 10;

    private final int length;

    private DataTypeNumeric(int length) {
        this.length = length;
    }

    public static DataTypeNumeric withNumberLength(int length) {
        return new DataTypeNumeric(length);
    }

    @Override
    public HighnoteFieldProcessResult<Integer> process(String substring) {
        return new HighnoteFieldProcessResult<>(
                length,
                substring.substring(0, length),
                Integer.parseInt(substring.substring(0, length), BASE_TEN_RADIX)
        );
    }

    @Override
    public String toString() {
        return "DataTypeNumeric{" +
                "length=" + length +
                '}';
    }
}
