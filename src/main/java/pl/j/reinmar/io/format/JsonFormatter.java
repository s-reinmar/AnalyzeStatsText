package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.io.util.TimeUtil;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Implementacja interfejsu {@link Formatter} formatująca dane wyjściowe do struktury JSON (JavaScript Object Notation).
 * <p>
 * Klasa generuje dokumenty JSON zawierające sygnaturę czasową wygenerowania raportu
 * (uzyskiwaną z {@link TimeUtil#now()}), odpowiednio sformatowane wartości numeryczne oraz
 * klucze słownika zeskapowane przed znakami specjalnymi za pomocą {@link Escape#json(String)}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class JsonFormatter implements Formatter {

    /**
     * Domyślny konstruktor formatera JSON.
     */
    public JsonFormatter() {
    }

    /**
     * Formatuj podstawowe statystyki tekstu do postaci obiektu JSON.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @return ciąg znaków w formacie JSON zawierający sekcję metryk oraz metadane
     */
    @Override
    public String formatBasic(TextStats stats) {
        return """
                {
                  "type": "basic_stats",
                  "generatedAt": "%s",
                  "stats": {
                    "words": %d,
                    "charsWithSpaces": %d,
                    "charsWithoutSpaces": %d,
                    "sentences": %d
                  }
                }
                """.formatted(
                TimeUtil.now(),
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        );
    }

    /**
     * Formatuj pełny raport (statystyki podstawowe oraz słownik częstotliwości) do postaci obiektu JSON.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @param freq  mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return ciąg znaków w formacie JSON zawierający podpunkty statystyk oraz mapę częstotliwości
     */
    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("""
                {
                  "type": "full_stats",
                  "generatedAt": "%s",
                  "stats": {
                    "words": %d,
                    "charsWithSpaces": %d,
                    "charsWithoutSpaces": %d,
                    "sentences": %d
                  },
                  "frequency": {
                """.formatted(
                TimeUtil.now(),
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        ));

        freq.forEach((w, c) ->
                sb.append("    \"%s\": %d,\n".formatted(Escape.json(w), c))
        );

        if (!freq.isEmpty()) sb.setLength(sb.length() - 2); // usuń ostatni przecinek

        sb.append("\n  }\n}");
        return sb.toString();
    }

    /**
     * Formatuj samą mapę częstotliwości występowania słów do postaci obiektu JSON.
     *
     * @param freq mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return ciąg znaków w formacie JSON reprezentujący słownik klucz-wartość dla częstotliwości
     */
    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("""
                {
                  "type": "frequency",
                  "generatedAt": "%s",
                  "frequency": {
                """.formatted(TimeUtil.now()));

        freq.forEach((w, c) ->
                sb.append("    \"%s\": %d,\n".formatted(Escape.json(w), c))
        );

        if (!freq.isEmpty()) sb.setLength(sb.length() - 2);

        sb.append("\n  }\n}");
        return sb.toString();
    }
}