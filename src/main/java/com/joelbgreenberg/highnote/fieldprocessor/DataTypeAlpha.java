package com.joelbgreenberg.highnote.fieldprocessor;

public class DataTypeAlpha implements IHighnoteFieldProcessor<String> {
    private final int length;

    private DataTypeAlpha(int length) {
        this.length = length;
    }

    public static DataTypeAlpha withAlphaLength(int length) {
        return new DataTypeAlpha(length);
    }

    @Override
    public HighnoteFieldProcessResult<String> process(String input) {
        return new HighnoteFieldProcessResult<>(
                this.length,
                input.substring(0, this.length),
                input.substring(0, this.length)
        );
    }

    @Override
    public String toString() {
        return "DataTypeAlpha{" +
                "length=" + length +
                '}';
    }
}
