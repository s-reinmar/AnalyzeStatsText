package pl.j.reinmar.core;

import java.util.List;

/**
 * Interfejs odpowiedzialny za podział znormalizowanego tekstu na poszczególne słowa (tokeny).
 * <p>
 * Służy jako abstrakcja dla algorytmów podziału tekstu, umożliwiając stosowanie
 * różnych strategii segmentacji wyrazów (np. po białych znakach, granicach słów regex itp.).
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
@FunctionalInterface
public interface Tokenizer {

    /**
     * Dzieli znormalizowany tekst wejściowy na listę słów (tokenów).
     *
     * @param normalizedText znormalizowany tekst do podziału
     * @return lista wyodrębnionych słów w postaci obiektów {@link String};
     *         zwraca pustą listę, jeśli tekst jest pusty lub wynosi {@code null}
     */
    List<String> words(String normalizedText);
}