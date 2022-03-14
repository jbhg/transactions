package com.joelbgreenberg.highnote.fieldprocessor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTypeLLVARTest {

    @Test
    public void testLLVARWithNumericTwoAndFourElements() {
        // Arrange
        IHighnoteFieldProcessor<String> processor = DataTypeLLVAR.withLengthOfNumericComponent(2);

        // Act
        HighnoteFieldProcessResult<String> result = processor.process("04CITY");

        // Assert
        assertEquals(result.getNewIndex(), 6);
        assertEquals(result.getParsedContent(), "04CITY");
        assertEquals(result.getRawContent(), "04CITY");
    }
}
