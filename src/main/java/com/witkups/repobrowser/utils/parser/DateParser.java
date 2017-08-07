package com.witkups.repobrowser.utils.parser;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Service
public final class DateParser implements Parser<String, String> {

    private final static DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public String parse(String isoDateTime, ZoneId zoneId, Locale locale) {
        final LocalDateTime localDateTime = parseDateToLocaleTime(isoDateTime);
        final DateTimeFormatter dateFormatter = prepareLocalizedDateFormatter(locale);
        return dateFormatter.format(localDateTime.atZone(zoneId));
    }

    private DateTimeFormatter prepareLocalizedDateFormatter(Locale locale) {
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        if (locale != null) {
            dateFormatter = dateFormatter.withLocale(locale);
        }
        return dateFormatter;
    }

    private LocalDateTime parseDateToLocaleTime(String date) {
        return LocalDateTime.parse(date, isoFormatter);

    }

}
