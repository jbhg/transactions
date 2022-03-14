package com.joelbgreenberg.highnote.fieldprocessor;

public class DataTypeLLVAR implements IHighnoteFieldProcessor<String> {
    private final int length;

    private DataTypeLLVAR(int lengthOfNumericComponent) {
        this.length = lengthOfNumericComponent;
    }

    public static DataTypeLLVAR withLengthOfNumericComponent(int length) {
        return new DataTypeLLVAR(length);
    }

    @Override
    public HighnoteFieldProcessResult<String> process(String input) {
        // first, lob off the first n digits.
        final IHighnoteFieldProcessor<Integer> numeric = DataTypeNumeric.withNumberLength(this.length);
        final HighnoteFieldProcessResult<Integer> numericResult = numeric.process(input);
        final int lengthToProcess = numericResult.getParsedContent();
        final int totalLength =
                this.length + lengthToProcess;

        return new HighnoteFieldProcessResult<>(
                totalLength,
                input.substring(0, totalLength),
                input.substring(0, totalLength)
        );
    }

    @Override
    public String toString() {
        return "DataTypeLLVAR{" +
                "length=" + length +
                '}';
    }
}
