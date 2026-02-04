package io.github.janhalasa.paybysquare.model;

public enum DirectDebitScheme {
    OTHER(0),
    SEPA(1);

    private final int code;

    DirectDebitScheme(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
