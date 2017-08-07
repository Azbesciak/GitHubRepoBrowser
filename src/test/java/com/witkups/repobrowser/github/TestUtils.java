package com.witkups.repobrowser.github;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

class TestUtils {

    static String parseDateWithLocale(String dateString, Locale locale){
        final LocalDateTime tempData = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale);
        final ZonedDateTime zonedDateTime = tempData.atZone(ZoneId.systemDefault());
        return formatter.format(zonedDateTime);
    }

}
