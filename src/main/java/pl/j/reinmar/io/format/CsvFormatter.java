package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Implementacja interfejsu {@link Formatter} formatująca dane wyjściowe do formatu CSV (Comma-Separated Values).
 * <p>
 * Klasa generuje tekst w postaci struktur tabelarycznych z nagłówkami kolumn,
 * wykorzystując klasę pomocniczą {@link Escape} do bezpiecznego eskapowania
 * słów zawierających znaki specjalne (np. przecinki, znaki nowej linii czy cudzysłowy).
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class CsvFormatter implements Formatter {

    /**
     * Domyślny konstruktor formatera CSV.
     */
    public CsvFormatter() {
    }

    /**
     * Formatuj podstawowe statystyki tekstu do postaci dwukolumnowej tabeli CSV (metryka, wartość).
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @return ciąg znaków w formacie CSV zawierający zestawienie metryk
     */
    @Override
    public String formatBasic(TextStats stats) {
        return "metric,value\n" +
                "words," + stats.words() + "\n" +
                "chars_with_spaces," + stats.charsWithSpaces() + "\n" +
                "chars_without_spaces," + stats.charsWithoutSpaces() + "\n" +
                "sentences," + stats.sentences() + "\n";
    }

    /**
     * Formatuj pełny raport (statystyki podstawowe oraz słownik częstotliwości) do formatu CSV.
     *
     * @param stats podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @param freq  mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return ciąg znaków w formacie CSV zawierający sekcję metryk oraz tabelę słów
     */
    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder(formatBasic(stats));
        sb.append("\nword,count\n");
        freq.forEach((w, c) -> sb.append(Escape.csv(w)).append(",").append(c).append("\n"));
        return sb.toString();
    }

    /**
     * Formatuj samą mapę częstotliwości występowania słów do postaci dwukolumnowej tabeli CSV (słowo, ilość).
     *
     * @param freq mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return ciąg znaków w formacie CSV reprezentujący listę słów i ich zliczeń
     */
    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("word,count\n");
        freq.forEach((w, c) -> sb.append(Escape.csv(w)).append(",").append(c).append("\n"));
        return sb.toString();
    }
}