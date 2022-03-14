package com.joelbgreenberg.highnote.dataelement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTypeTest {

    @Test
    public void testParsing() {
        assertEquals(MessageType.AUTH_REQUEST, MessageType.parseCode(MessageType.AUTH_REQUEST.getCode()).get());
        assertEquals(MessageType.AUTH_RESPONSE, MessageType.parseCode(MessageType.AUTH_RESPONSE.getCode()).get());
    }
}