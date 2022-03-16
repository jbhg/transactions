package com.joelbgreenberg.highnote.dataelement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputMessageTest {

    @Test
    void testFirstOutputLine() {
        // Arrange
        final String input = "0100e016411111111111111112250000001000";
        final InputMessage inputMessage = InputMessage.createFromString(input);
        final String EXPECTED_BITMAP = "11100000";
        // Act

        // Assert
        assertNotNull(inputMessage);
        assertEquals(inputMessage.getMessageType(), MessageType.AUTH_REQUEST);
        assertEquals(inputMessage.getFieldsBitmap().getBitmap(), EXPECTED_BITMAP);
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(0), "0 index is out of bounds and should be false.");
        assertTrue(inputMessage.getFieldsBitmap().isBitSetToTrue(1), EXPECTED_BITMAP + ": 1 index is set to 1 and should be true.");
        assertTrue(inputMessage.getFieldsBitmap().isBitSetToTrue(2), EXPECTED_BITMAP + ": 2 index is set to 1 and should be true.");
        assertTrue(inputMessage.getFieldsBitmap().isBitSetToTrue(3), EXPECTED_BITMAP + ": 3 index is set to 1 and should be true.");
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(4));
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(5));
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(6));
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(7));
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(8));
        assertFalse(inputMessage.getFieldsBitmap().isBitSetToTrue(9));
    }

    @Test
    void testSecondOutputLine() {
        // Arrange
        final String input = "0100e016401288888888188112250000011000";
        final InputMessage inputMessage = InputMessage.createFromString(input);

        // Act

        // Assert
        assertNotNull(inputMessage);
        assertEquals(inputMessage.getMessageType(), MessageType.AUTH_REQUEST);
        assertEquals(inputMessage.getFieldsBitmap().getBitmap(), "11100000");

    }
    @Test
    void testThirdOutputLine() {
        // Arrange
        final String input = "0100ec1651051051051051001225000001100011MASTER YODA90089";
        final InputMessage inputMessage = InputMessage.createFromString(input);

        // Act

        // Assert
        assertNotNull(inputMessage);
        assertEquals(inputMessage.getMessageType(), MessageType.AUTH_REQUEST);
        assertEquals(inputMessage.getFieldsBitmap().getBitmap(), "11101100");

    }
    @Test
    void testFifthOutputLine() {
        // Arrange
        final String input = "01006012250000001000";
        final InputMessage inputMessage = InputMessage.createFromString(input);

        // Act

        // Assert
        assertNotNull(inputMessage);
        assertEquals(inputMessage.getMessageType(), MessageType.AUTH_REQUEST);
        assertEquals(inputMessage.getFieldsBitmap().getBitmap(), "01100000");
    }
}