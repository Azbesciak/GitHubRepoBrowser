package com.witkups.repobrowser.utils;

import java.time.ZoneId;
import java.util.TimeZone;

public final class TimeZoneUtils {

    public static ZoneId getTimeZoneIdOrDefault(TimeZone timeZone) {
        return timeZone != null ? timeZone.toZoneId() : TimeZone.getDefault().toZoneId();
    }

}
