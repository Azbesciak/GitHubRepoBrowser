package com.witkups.repobrowser.utils.parser;

import java.time.ZoneId;
import java.util.Locale;

public interface Parser<S,T> {

    T parse(S source, ZoneId zoneId, Locale locale);

    default T parse(S source) {
        final Locale locale = Locale.getDefault();
        final ZoneId zoneId = ZoneId.systemDefault();
        return parse(source, zoneId, locale);
    }

}
