package com.joelbgreenberg.highnote;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteHexUtilsTest {

    @Test
    public void testHexToDecimal() {
        assertEquals(ByteHexUtils.hexToInt("e0"), 224);
    }

    @Test
    public void testHexToBinary() {
        assertEquals(ByteHexUtils.hexTobytes("e0"), "11100000");
        assertEquals(ByteHexUtils.hexTobytes("ec"), "11101100");
        assertEquals(ByteHexUtils.hexTobytes("f0"), "11110000");
        assertEquals(ByteHexUtils.hexTobytes("60"), "01100000");
    }
}