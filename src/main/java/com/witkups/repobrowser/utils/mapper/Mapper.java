package com.witkups.repobrowser.utils.mapper;

import java.time.ZoneId;
import java.util.Locale;

public interface Mapper<T, S> {

    T map(S source, ZoneId zoneId, Locale locale);

    default T map(S source) {
        Locale locale = Locale.getDefault();
        ZoneId zoneId = ZoneId.systemDefault();
        return map(source, zoneId, locale);
    }
}
