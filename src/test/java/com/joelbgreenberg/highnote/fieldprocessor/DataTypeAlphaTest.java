package com.joelbgreenberg.highnote.fieldprocessor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataTypeAlphaTest {

    @Test
    public void testAlphaWithContent() {
        // Arrange
        IHighnoteFieldProcessor<String> processor = DataTypeAlpha.withAlphaLength(9);

        // Act
        HighnoteFieldProcessResult<String> result = processor.process("DALLAS   ");

        // Assert
        assertEquals(result.getNewIndex(), 9);
        assertEquals(result.getParsedContent(), "DALLAS   ");
        assertEquals(result.getRawContent(), "DALLAS   ");
    }
}
