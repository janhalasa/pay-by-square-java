package io.github.janhalasa.paybysquare.model;

public enum Periodicity {

    DAILY("d"),
    WEEKLY("w"),
    BIWEEKLY("b"),
    MONTHLY("m"),
    BIMONTHLY("B"),
    QUARTERLY("q"),
    SEMIANNUALLY("s"),
    ANNUALLY("a");

    private final String code;

    Periodicity(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
