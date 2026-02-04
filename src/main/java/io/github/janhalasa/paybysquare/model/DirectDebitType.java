package io.github.janhalasa.paybysquare.model;

public enum DirectDebitType {

    ONE_OFF(0),
    RECURRENT(1);

    private final int code;

    DirectDebitType(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
