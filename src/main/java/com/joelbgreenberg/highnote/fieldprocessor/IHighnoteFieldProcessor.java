package com.joelbgreenberg.highnote.fieldprocessor;

public interface IHighnoteFieldProcessor<T> {

    HighnoteFieldProcessResult<T> process(String substring);
}
