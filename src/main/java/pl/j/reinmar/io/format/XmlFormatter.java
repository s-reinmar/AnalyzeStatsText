package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.io.util.TimeUtil;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Implementacja interfejsu {@link Formatter} formatująca dane wyjściowe do struktury XML (Extensible Markup Language).
 * <p>
 * Klasa generuje dokumenty XML zawierające sygnaturę czasową wygenerowania raportu
 * (uzyskiwaną z {@link TimeUtil#now()}), sekcje statystyk oraz znaki specjalne w atrybutach
 * tekstu poprawnie zabezpieczone przed niepoprawną składnią XML za pomocą {@link Escape#xml(String)}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class XmlFormatter implements Formatter {

    /**
     * Domyślny konstruktor formatera XML.
     */
    public XmlFormatter() {
    }

    /**
     * Formatuj podstawowe statystyki tekstu do postaci dokumentu XML.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @return ciąg znaków w formacie XML zawierający znacznik główny z metrykami i znacznikiem czasu
     */
    @Override
    public String formatBasic(TextStats stats) {
        return """
                <report type="basic_stats" generatedAt="%s">
                  <stats>
                    <words>%d</words>
                    <charsWithSpaces>%d</charsWithSpaces>
                    <charsWithoutSpaces>%d</charsWithoutSpaces>
                    <sentences>%d</sentences>
                  </stats>
                </report>
                """.formatted(
                TimeUtil.now(),
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        );
    }

    /**
     * Formatuj pełny raport (statystyki podstawowe oraz słownik częstotliwości) do postaci dokumentu XML.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @param freq  mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return ciąg znaków w formacie XML zawierający elementy statystyk oraz listę węzłów słów
     */
    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("""
                <report type="full_stats" generatedAt="%s">
                  <stats>
                    <words>%d</words>
                    <charsWithSpaces>%d</charsWithSpaces>
                    <charsWithoutSpaces>%d</charsWithoutSpaces>
                    <sentences>%d</sentences>
                  </stats>
                  <frequency>
                """.formatted(
                TimeUtil.now(),
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        ));

        freq.forEach((w, c) ->
                sb.append("    <word text=\"%s\" count=\"%d\" />\n"
                        .formatted(Escape.xml(w), c))
        );

        sb.append("  </frequency>\n</report>");
        return sb.toString();
    }

    /**
     * Formatuj samą mapę częstotliwości występowania słów do postaci dokumentu XML.
     *
     * @param freq mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return ciąg znaków w formacie XML reprezentujący węzeł częstotliwości i podrzędne elementy słów
     */
    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("""
                <report type="frequency" generatedAt="%s">
                  <frequency>
                """.formatted(TimeUtil.now()));

        freq.forEach((w, c) ->
                sb.append("    <word text=\"%s\" count=\"%d\" />\n"
                        .formatted(Escape.xml(w), c))
        );

        sb.append("  </frequency>\n</report>");
        return sb.toString();
    }
}