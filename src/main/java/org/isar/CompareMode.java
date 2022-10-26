package org.isar;

import java.util.Arrays;

public enum CompareMode {

    SIZE("0"), NAME("1"), SIZE_AND_NAME("2");

    private final String mode;

    CompareMode(String mode) {
        this.mode = mode;
    }

    public static CompareMode from(String mode) {
        return Arrays.stream(CompareMode.values())
                .filter(compareMode -> compareMode.mode.equals(mode)).findFirst()
                .orElse(CompareMode.SIZE_AND_NAME);
    }
}
