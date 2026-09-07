package pl.j.reinmar.core;

import java.util.Objects;

/**
 * Domyślna implementacja interfejsu {@link Normalizer} usuwająca znaki interpunkcyjne.
 * <p>
 * Klasa sprowadza przekazany tekst do małych liter, przycina skrajne białe znaki,
 * a wszystkie znaki interpunkcyjne (standardowe ASCII oraz wybrane znaki cudzysłowów
 * typograficznych) zastępuje pojedynczymi spacjami.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class DefaultNormalizer implements Normalizer {

    /**
     * Wyrażenie regularne dopasowujące standardową interpunkcję ASCII oraz znaki cudzysłowów typograficznych.
     */
    private static final String PUNCT_REGEX = "[\\p{Punct}„”»«]";

    /**
     * Domyślny konstruktor klasy normalizującej tekst.
     */
    public DefaultNormalizer() {
    }

    /**
     * Normalizuje podany ciąg znaków.
     * <p>
     * Zwraca pusty ciąg znaków, jeśli przekazany parametr jest równy {@code null} lub pusty.
     * W przeciwnym razie usuwa skrajne spacje, zamienia litery na małe oraz zastępuje znaki
     * interpunkcyjne spacjami.
     * </p>
     *
     * @param text tekst wejściowy do znormalizowania
     * @return znormalizowany tekst w postaci ciągu znaków
     */
    @Override
    public String normalize(String text) {
        String t = Objects.requireNonNullElse(text, "").trim();
        if (t.isEmpty()) return "";
        return t.toLowerCase().replaceAll(PUNCT_REGEX, " ");
    }
}