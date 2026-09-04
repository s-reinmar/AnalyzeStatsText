package pl.j.reinmar.io.builder;

import pl.j.reinmar.io.format.Formatter;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Buduje treść raportów w zależności od typu raportu i formatu.
 * Deleguje formatowanie do odpowiedniego Formattera.
 */
public class ReportBuilder {

    /**
     * Główna metoda budująca raport.
     *
     * @param type   typ raportu (BASIC, FULL, FREQUENCY)
     * @param stats  statystyki tekstu (dla BASIC i FULL)
     * @param freq   częstotliwości słów (dla FULL i FREQUENCY)
     * @param f      formatter odpowiedzialny za format wyjściowy
     * @return gotowa treść raportu
     */
    public static String build(ReportType type,
                               TextStats stats,
                               Map<String, Integer> freq,
                               Formatter f) {

        return switch (type) {
            case BASIC -> f.formatBasic(stats);
            case FULL -> f.formatFull(stats, freq);
            case FREQUENCY -> f.formatFrequency(freq);
        };
    }
}