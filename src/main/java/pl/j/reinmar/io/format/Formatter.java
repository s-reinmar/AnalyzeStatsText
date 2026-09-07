package pl.j.reinmar.io.format;

import pl.j.reinmar.model.TextStats;

import java.util.Map;

/**
 * Interfejs odpowiadający za przekształcanie statystyk i wyników analizy tekstu na sformatowany tekst.
 * <p>
 * Służy jako abstrakcja dla różnych struktur wyjściowych (np. TXT, CSV, JSON, XML).
 * Pozwala na generowanie raportów w trzech wariantach zawartości: podstawowym,
 * pełnym oraz ograniczonym wyłącznie do analizy częstotliwościowej.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public interface Formatter {

    /**
     * Formatuj podstawowe statystyki ilościowe tekstu.
     *
     * @param stats podstawowe statystyki ilościowe ({@link TextStats})
     * @return sformatowana reprezentacja tekstowa statystyk podstawowych
     */
    String formatBasic(TextStats stats);

    /**
     * Formatuj pełny raport obejmujący zarówno statystyki podstawowe, jak i zestawienie częstotliwości słów.
     *
     * @param stats podstawowe statystyki ilościowe ({@link TextStats})
     * @param freq  mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return sformatowana reprezentacja tekstowa pełnego raportu
     */
    String formatFull(TextStats stats, Map<String, Integer> freq);

    /**
     * Formatuj wyłącznie zestawienie częstotliwości występowania słów.
     *
     * @param freq mapa częstotliwości występowania słów (słowo -&gt; liczba wystąpień)
     * @return sformatowana reprezentacja tekstowa analizy częstotliwościowej
     */
    String formatFrequency(Map<String, Integer> freq);
}