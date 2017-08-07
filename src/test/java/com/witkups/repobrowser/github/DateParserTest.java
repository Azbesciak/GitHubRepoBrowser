package com.witkups.repobrowser.github;

import com.witkups.repobrowser.utils.parser.DateParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

import static com.witkups.repobrowser.github.TestUtils.parseDateWithLocale;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DateParserTest {

    private static final ZoneId defaultZoneId = ZoneId.systemDefault();

    @Autowired
    private DateParser dateParser;

    @Test
    public void mapLocalizedDateTest() {
        final String localDateTime = Instant.now().toString();

        final String createdAtChina = dateParser.parse(localDateTime, defaultZoneId, Locale.CHINA);
        final String createdAtCanada = dateParser.parse(localDateTime, defaultZoneId, Locale.CANADA);
        final String createdAtFrench = dateParser.parse(localDateTime, defaultZoneId, Locale.FRENCH);

        final String chinaTime = parseDateWithLocale(localDateTime, Locale.CHINA);
        final String canadaTime = parseDateWithLocale(localDateTime, Locale.CANADA);
        final String frenchTime = parseDateWithLocale(localDateTime, Locale.FRENCH);

        assertEquals(chinaTime, createdAtChina);
        assertEquals(canadaTime, createdAtCanada);
        assertEquals(frenchTime, createdAtFrench);
    }

}
