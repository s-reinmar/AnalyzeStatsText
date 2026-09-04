package pl.j.reinmar.io.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static String now() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}