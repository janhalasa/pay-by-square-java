package io.github.janhalasa.paybysquare.model;

public enum MonthClassifier {

    JANUARY(1),
    FEBRUARY(2),
    MARCH(4),
    APRIL(8),
    MAY(16),
    JUNE(32),
    JULY(64),
    AUGUST(128),
    SEPTEMBER(256),
    OCTOBER(512),
    NOVEMBER(1024),
    DECEMBER(2048);

    private final int code;

    MonthClassifier(int value) {
        this.code = value;
    }

    public int code() {
        return code;
    }
}
