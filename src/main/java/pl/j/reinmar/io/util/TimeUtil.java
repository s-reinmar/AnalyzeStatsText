package pl.j.reinmar.io.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    /**
     * Zwraca aktualny czas w formacie ISO-8601 z przesunięciem strefowym.
     *
     * @return aktualny czas jako String, np. "2025-01-01T12:34:56+01:00"
     */
    public static String now() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}