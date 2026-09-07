package pl.j.reinmar.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Podstawowa implementacja interfejsu {@link Tokenizer} dzieląca tekst po białych znakach.
 * <p>
 * Klasa wykorzystuje wyrażenie regularne dopasowujące ciągi białych znaków (spacje, tabulatory,
 * znaki nowej linii), wyodrębniając poszczególne słowa z uprzednio znormalizowanego tekstu.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class WhitespaceTokenizer implements Tokenizer {

    /** Wyrażenie regularne dopasowujące jeden lub więcej białych znaków. */
    private static final String WHITESPACE_REGEX = "\\s+";

    /**
     * Domyślny konstruktor tokenizatora dzielącego tekst po białych znakach.
     */
    public WhitespaceTokenizer() {
    }

    /**
     * Dzieli znormalizowany tekst na listę słów na podstawie białych znaków.
     * <p>
     * Przed podziałem usuwa skrajne białe znaki. Zwraca pustą listę, jeśli przekazany
     * tekst jest równy {@code null} lub zawiera wyłącznie białe znaki.
     * </p>

     * @param normalizedText znormalizowany tekst wejściowy
     * @return modyfikowalna lista wyodrębnionych słów
     */
    @Override
    public List<String> words(String normalizedText) {
        if (normalizedText == null || normalizedText.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] parts = normalizedText.trim().split(WHITESPACE_REGEX);
        return new ArrayList<>(Arrays.asList(parts));
    }
}