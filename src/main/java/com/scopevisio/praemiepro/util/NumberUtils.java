package com.scopevisio.praemiepro.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtils {

    public static String formatToGermanNumber(final Number number) {
        return NumberFormat.getNumberInstance(Locale.GERMANY).format(number);
    }
}
