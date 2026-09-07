package pl.j.reinmar.io.format;

import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Implementacja interfejsu {@link Formatter} formatująca dane wyjściowe do postaci czytelnego tekstu niesformatowanego (TXT).
 * <p>
 * Klasa generuje raporty w formie czytelnych dla człowieka nagłówków oraz wyrównanych kolumn tekstowych,
 * idealnych do bezpośredniego wyświetlania w konsoli lub zapisywania w plikach tekstowych.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class TxtFormatter implements Formatter {

    /**
     * Domyślny konstruktor formatera tekstowego TXT.
     */
    public TxtFormatter() {
    }

    /**
     * Formatuj podstawowe statystyki tekstu do postaci bloku tekstowego z nagłówkiem.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @return sformatowany ciąg znaków z podsumowaniem liczby słów, znaków oraz zdań
     */
    @Override
    public String formatBasic(TextStats stats) {
        return """
                === STATYSTYKI ===
                Słowa: %d
                Znaki (ze spacjami): %d
                Znaki (bez spacji): %d
                Zdania: %d
                """.formatted(
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        );
    }

    /**
     * Formatuj pełny raport obejmujący podsumowanie statystyk oraz wyrównane zestawienie częstotliwości słów.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @param freq  mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return sformatowany ciąg znaków zawierający obie sekcje raportu
     */
    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder(formatBasic(stats));
        sb.append("\n=== CZĘSTOTLIWOŚCI ===\n");
        freq.forEach((w, c) -> sb.append("%-20s : %d%n".formatted(w, c)));
        return sb.toString();
    }

    /**
     * Formatuj samą mapę częstotliwości występowania słów do postaci wyrównanego zestawienia z nagłówkiem.
     *
     * @param freq mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return sformatowany ciąg znaków z listą słów i wyznaczoną liczbą ich wystąpień
     */
    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("=== CZĘSTOTLIWOŚCI ===\n");
        freq.forEach((w, c) -> sb.append("%-20s : %d%n".formatted(w, c)));
        return sb.toString();
    }
}