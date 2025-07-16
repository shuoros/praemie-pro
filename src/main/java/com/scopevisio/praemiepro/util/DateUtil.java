package com.scopevisio.praemiepro.util;

import com.scopevisio.praemiepro.config.Constants;
import com.scopevisio.praemiepro.domain.Order;

public final class DateUtil {

    public static String formatDate(final Order order) {
        return order.getCreatedDate().atZone(Constants.DEFAULT_ZONE_ID).format(Constants.DEFAULT_DAY_MONTH_YEAR_FORMATTER);
    }
}
