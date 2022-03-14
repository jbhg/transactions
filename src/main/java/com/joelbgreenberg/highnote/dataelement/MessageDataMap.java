package com.joelbgreenberg.highnote.dataelement;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.joelbgreenberg.highnote.fieldprocessor.HighnoteFieldProcessResult;

public class MessageDataMap {

    private final ImmutableMap<DataType, HighnoteFieldProcessResult<?>> data;

    private MessageDataMap(ImmutableMap<DataType, HighnoteFieldProcessResult<?>> data) {
        this.data = data;
    }

    public static MessageDataMap from(ImmutableMap<DataType, HighnoteFieldProcessResult<?>> build) {
        return new MessageDataMap(build);
    }

    public HighnoteFieldProcessResult<?> get(DataType dataType) {
        return data.get(dataType);
    }

    public boolean containsKey(DataType dataType) {
        return data.containsKey(dataType);
    }

    public ImmutableSet<DataType> keySet() {
        return ImmutableSet.copyOf(data.keySet());
    }

    public ImmutableMap<DataType, HighnoteFieldProcessResult<?>> getDataCopy() {
        return ImmutableMap.copyOf(data);
    }
}
