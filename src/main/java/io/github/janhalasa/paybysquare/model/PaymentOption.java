package io.github.janhalasa.paybysquare.model;

public enum PaymentOption {

    PAYMENT_ORDER(1),
    STANDING_ORDER(2),
    DIRECT_DEBIT(4);

    private final int code;

    PaymentOption(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
