package pl.j.reinmar.core;

/**
 * Określa tryb wyjściowy oraz strukturę zwracanych danych podczas analizy słów w {@link pl.j.reinmar.core.TextAnalyzer}.
 * <p>
 * W zależności od wybranej wartości metoda analizująca zwraca słownik z nieposortowanymi
 * lub posortowanymi częstotliwościami, ograniczone zestawienie najczęstszych wyrazów (Top N)
 * bądź pełną listę zanalizowanych słów.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public enum WordAnalysisMode {

    /**
     * Zwraca zbiór słów wraz z ich częstotliwością w postaci nieposortowanej mapy ({@code Map<String, Integer>}).
     */
    FREQUENCY_MAP,

    /**
     * Zwraca zbiór słów wraz z ich częstotliwością w postaci mapy posortowanej według wskazanego kryterium ({@code Map<String, Integer>}).
     */
    FREQUENCY_SORTED,

    /**
     * Zwraca listę obiektów {@link WordCount} ograniczoną do określonej liczby (N) najczęstszych słów ({@code List<WordCount>}).
     */
    TOP_WORDS,

    /**
     * Zwraca pełną, posortowaną listę wszystkich przeanalizowanych słów w postaci obiektów {@link WordCount} ({@code List<WordCount>}).
     */
    ALL_WORDS_SORTED
}