package pl.j.reinmar.io.builder;

import pl.j.reinmar.io.format.Formatter;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Klasa pomocnicza (budowniczego) odpowiedzialna za składanie treści raportów na podstawie typu i formatu.
 * <p>
 * Wykorzystuje wzorzec fabryki/budowniczego do przekierowania procesu generowania zawartości
 * raportu do odpowiedniej metody formatującej interfejsu {@link Formatter} w zależności
 * od wskazanego wariantu {@link ReportType}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class ReportBuilder {

    /**
     * Domyślny konstruktor klasy budującej raporty.
     */
    public ReportBuilder() {
    }

    /**
     * Buduje treść raportu tekstowego na podstawie wybranego typu, statystyk oraz formatera.
     *
     * @param type  typ generowanego raportu ({@link ReportType})
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @param freq  mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @param f     komponent formatujący treść wyjściową ({@link Formatter})
     * @return gotowy do zapisu lub wyświetlenia ciąg znaków reprezentujący raport
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