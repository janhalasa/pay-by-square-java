package io.github.janhalasa.paybysquare.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayBySquareSerializerTest {

    @Test
    void givenComplicatedDecimalNumberAsAmount_whenFormated_thenAllNumbersUsed() {
        String expected = "12345.1234";
        BigDecimal bd = new BigDecimal(expected);

        String result = PayBySquareSerializer.DECIMAL_FORMAT.format(bd);
        assertEquals(expected, result);
    }
}
