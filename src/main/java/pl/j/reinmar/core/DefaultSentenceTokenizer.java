package pl.j.reinmar.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Domyślna implementacja interfejsu {@link SentenceTokenizer} dzieląca tekst na zdania.
 * <p>
 * Klasa wykorzystuje proste wyrażenie regularne dopasowujące standardowe znaki
 * kończące zdanie (kropka, wykrzyknik, znak zapytania). Puste fragmenty i nadmiarowe
 * białe znaki są automatycznie usuwane z wyników.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class DefaultSentenceTokenizer implements SentenceTokenizer {

    /**
     * Domyślny konstruktor tokenizatora zdań.
     */
    public DefaultSentenceTokenizer() {
    }

    /**
     * Dzieli przekazany tekst na listę pojedynczych zdań.
     * <p>
     * Zwraca pustą listę w przypadku przekazania wartości {@code null} lub pustego ciągu znaków.
     * Metoda dzieli tekst na podstawie wystąpienia jednego lub więcej znaków z grupy {@code [.!?]},
     * a następnie ucina skrajne białe znaki z każdego wyodrębnionego zdania.
     * </p>
     *
     * @param text tekst wejściowy do podzielenia na zdania
     * @return lista wyodrębnionych, niepustych zdań
     */
    @Override
    public List<String> sentences(String text) {
        List<String> result = new ArrayList<>();
        if (text == null) return result;
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return result;

        // Proste dzielenie po . ! ?
        String[] parts = trimmed.split("[.!?]+");
        for (String p : parts) {
            String s = p.trim();
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }
}