package com.scopevisio.praemiepro;

import java.math.BigDecimal;

public abstract class AbstractTest {

    protected static final String VALID_ZIPCODE = "50374";
    protected static final String INVALID_ZIPCODE = "00000";
    protected static final String ADMIN_EMAIL = "admin@email.com";
    protected static final String USER_EMAIL = "user@email.com";
    protected static final String USER_EMAIL_2 = "user2@email.com";
    public static final String PASSWORD = "test";

    protected static BigDecimal getBigDecimal(final Double value) {
        return BigDecimal.valueOf(value).setScale(2);
    }
}
