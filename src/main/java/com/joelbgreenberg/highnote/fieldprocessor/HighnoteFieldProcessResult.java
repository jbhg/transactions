package com.joelbgreenberg.highnote.fieldprocessor;

public class HighnoteFieldProcessResult<T> {

    private final int newIndex;
    private final String rawContent;
    private final T parsedContent;

    public HighnoteFieldProcessResult(int newIndex) {
        this(newIndex, null, null);
    }

    public HighnoteFieldProcessResult(int newIndex, String rawContent, T parsedContent) {
        this.newIndex = newIndex;
        this.rawContent = rawContent;
        this.parsedContent = parsedContent;
    }

    public int getNewIndex() {
        return newIndex;
    }

    public T getParsedContent() {
        return parsedContent;
    }

    public String getRawContent() {
        return rawContent;
    }

    @Override
    public String toString() {
        return "HighnoteFieldProcessResult{" +
                "newIndex=" + newIndex +
                ", rawContent='" + rawContent + '\'' +
                ", parsedContent=" + parsedContent +
                '}';
    }
}
