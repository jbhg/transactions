package com.joelbgreenberg.highnote;


public class ByteHexUtils {

    public static int hexToInt(String input) {
        return Integer.parseInt(input, 16);
    }

    public static String hexTobytes(String input) {
        final int parsed = Integer.parseInt(input, 16);
        final String asBinary = Integer.toBinaryString(parsed);
        StringBuilder result = new StringBuilder("");
        if (asBinary.length() < 8) {
            for(int i = 0; i < 8 - asBinary.length(); i++) {
                result.append("0");
            }
        }
        return result + asBinary;
    }

    public static String bytesToHex(String input) {
        return Integer.toHexString(Integer.parseInt(input, 2));
    }

}
