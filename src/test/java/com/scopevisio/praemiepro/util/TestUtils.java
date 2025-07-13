package com.scopevisio.praemiepro.util;

import java.math.BigDecimal;

public class TestUtils {

    public static BigDecimal getBigDecimal(final Double value) {
        return BigDecimal.valueOf(value).setScale(2);
    }
}
