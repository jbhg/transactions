package com.joelbgreenberg.highnote.fieldprocessor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataTypeNumericTest {
    @Test
    public void testNumericTwoWithFourElements() {
        // Arrange
        IHighnoteFieldProcessor<Integer> processor =  DataTypeNumeric.withNumberLength(4);

        // Act
        HighnoteFieldProcessResult<Integer> result = processor.process("0920");

        // Assert
        assertEquals(result.getNewIndex(), 4);
        assertEquals(result.getParsedContent(), 920);
        assertEquals(result.getRawContent(), "0920");
    }
}