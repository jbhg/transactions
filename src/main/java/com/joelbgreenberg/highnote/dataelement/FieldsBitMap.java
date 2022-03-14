package com.joelbgreenberg.highnote.dataelement;

import com.google.common.collect.ImmutableSet;
import com.joelbgreenberg.highnote.Configuration;

public class FieldsBitMap {
    private final String bitmap;

    private FieldsBitMap(String bitmap) {
        this.bitmap = bitmap;
    }

    public static FieldsBitMap from(ImmutableSet<Integer> bits) {
        StringBuilder builder = new StringBuilder();
        for(int s : Configuration.ORDERED_INDEXES) {
            builder.append( bits.contains(s) ? "1" : "0" );
        }
        return new FieldsBitMap(builder.toString());
    }

    public static FieldsBitMap from(String bitmap) {
        return new FieldsBitMap(bitmap);
    }

    public String getBitmap() {
        return bitmap;
    }

    public boolean isBitSetToTrue(int index) {
        if (index < 1 || index > bitmap.length()) {
            return false;
        }
        return bitmap.charAt(index - 1) == '1';
    }
}
