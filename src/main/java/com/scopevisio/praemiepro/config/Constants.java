package com.scopevisio.praemiepro.config;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final String SYSTEM = "system";

    public static final String DEFAULT_TIMEZONE = "Europe/Berlin";
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIMEZONE);
    public static final DateTimeFormatter DEFAULT_DAY_MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
}
