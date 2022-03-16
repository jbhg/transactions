package com.joelbgreenberg.highnote.dataelement;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.joelbgreenberg.highnote.ByteHexUtils;
import com.joelbgreenberg.highnote.Configuration;
import com.joelbgreenberg.highnote.fieldprocessor.HighnoteFieldProcessResult;
import com.joelbgreenberg.highnote.fieldprocessor.IHighnoteFieldProcessor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class InputMessage {

    private MessageType messageType;
    private FieldsBitMap fieldsBitmap;
    private MessageDataMap data;

    public static Optional<String> getSubstringSafe(String input, int start, int end) {
        try {
            return Optional.of(input.substring(start, end));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static InputMessage createFromMessageWithAuthCode(InputMessage message, MessageType messageType, ResponseCode code) {
        final InputMessage result = new InputMessage();
        result.setMessageType(messageType);
        result.setFieldsBitmap(message.getFieldsBitmap());
        result.setData(
                MessageDataMap.from(
                        ImmutableMap.<DataType, HighnoteFieldProcessResult<?>>builder()
                                .putAll(message.getData().getDataCopy())
                                .put(DataType.ResponseCode, new HighnoteFieldProcessResult<>(2, code.getCode(), code.getCode()))
                                .build())
        );
        return result;
    }

    public String serializeToString() {
        return Joiner.on("").join(ImmutableList.of(
                this.messageType.getCode(),
                ByteHexUtils.bytesToHex(FieldsBitMap.from((ImmutableSet<Integer>) this.data.keySet().stream().map(DataType::getBit).collect(collectingAndThen(toSet(), ImmutableSet::copyOf))).getBitmap())
        )) + Joiner.on("").join(Configuration.ORDERED_INDEXES.stream()
                .filter(idx -> DataType.fromBit(idx).isPresent() && this.data.containsKey(DataType.fromBit(idx).get()))
                .map(idx -> Pair.of(idx, DataType.fromBit(idx)))
                .filter(idx -> idx.getRight().isPresent())
                .map(idx -> Pair.of(idx.getLeft(), this.data.get(idx.getRight().get()).getRawContent()))
                .sorted(Comparator.comparing(Pair::getLeft))
                .map(Pair::getRight)
                .collect(toList()));
    }

    public static InputMessage createFromString(String input) {
        InputMessage result = new InputMessage();
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Message parsed to null");
        }

        // Find the Message Type indicator (4 chars)
        if (input.length() < 4) {
            throw new IllegalArgumentException("Input was not long enough to contain a message type.");
        }
        final Optional<MessageType> messageType = MessageType.parseCode(input.substring(0, 4));
        if (!messageType.isPresent()) {
            throw new IllegalArgumentException("MessageType was not found: " + input.substring(0, 4));
        }
        result.setMessageType(messageType.get());

        // Find the bitmap representing which data elements are present.
        final Optional<String> bitmap = getSubstringSafe(input, 4, 6);
        if (!bitmap.isPresent()) {
            throw new IllegalArgumentException("Bitmap not found.");
        }
        {
            // scope the fieldsBitmap variable to be unavailable below this use.
            final String fieldsBitmap = ByteHexUtils.hexTobytes(bitmap.get());
//            System.out.println(String.format("Bitmap: %s", fieldsBitmap));
            result.setFieldsBitmap(FieldsBitMap.from(fieldsBitmap));

        }
        final ImmutableMap.Builder<DataType, HighnoteFieldProcessResult<?>> dataBuilder = ImmutableMap.builder();
        final AtomicInteger cursor = new AtomicInteger(6);

        // for each data element present, validate and parse
        for(int index : Configuration.ORDERED_INDEXES) {
            if (result.getFieldsBitmap().isBitSetToTrue(index)) {
                // process this bit field.
                if (!DataType.fromBit(index).isPresent() || !Configuration.DATA_TYPE_STRATEGY_MAP.containsKey(DataType.fromBit(index).get())) {
                    throw new IllegalArgumentException("There is no processor strategy mapping associated with index " + index);
                }
                final IHighnoteFieldProcessor<?> processor = Configuration.DATA_TYPE_STRATEGY_MAP.get(DataType.fromBit(index).get());
                System.out.println(String.format("\tBit %d [cursor at %d]: %s", index, cursor.get(), processor));

                final HighnoteFieldProcessResult<?> processedResult = processor.process(input.substring(cursor.get()));
                System.out.println(String.format("\t\tResult: %s", processedResult));

                cursor.addAndGet(processedResult.getNewIndex());
                dataBuilder.put(DataType.fromBit(index).get(), processedResult);
            }
        }
        result.setData(MessageDataMap.from(dataBuilder.build()));

        // return all of this in an object representation.
        System.out.println(String.format("COMPLETE -> Parsing {%s} ===", input));
        return result;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public FieldsBitMap getFieldsBitmap() {
        return fieldsBitmap;
    }

    public void setFieldsBitmap(FieldsBitMap fieldsBitmap) {
        this.fieldsBitmap = fieldsBitmap;
    }

    public MessageDataMap getData() {
        return data;
    }

    public void setData(MessageDataMap data) {
        this.data = data;
    }
}
