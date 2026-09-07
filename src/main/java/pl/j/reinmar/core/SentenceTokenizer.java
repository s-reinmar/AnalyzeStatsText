package pl.j.reinmar.core;

import java.util.List;

/**
 * Interfejs odpowiedzialny za podział tekstu na pojedyncze zdania.
 * <p>
 * Służy jako abstrakcja dla algorytmów segmentacji tekstu wejściowego,
 * umożliwiając wyodrębnianie zdań na potrzeby szczegółowych statystyk
 * i analiz strukturalnych.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
@FunctionalInterface
public interface SentenceTokenizer {

    /**
     * Dzieli przekazany tekst na listę osobnych zdań.
     *
     * @param text tekst wejściowy do przetworzenia
     * @return lista wyodrębnionych zdań w postaci obiektów {@link String};
     *         zwraca pustą listę, jeśli tekst wynosi {@code null} lub jest pusty
     */
    List<String> sentences(String text);
}