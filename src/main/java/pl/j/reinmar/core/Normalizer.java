package pl.j.reinmar.core;

/**
 * Interfejs do normalizacji tekstu przed procesem analizy.
 * <p>
 * Odpowiada za wstępne przygotowanie ciągu znaków (np. konwersję liter na małe,
 * usuwanie znaków interpunkcyjnych lub oczyszczanie białych znaków), aby ujednolicić
 * formę wyrazów analizowanych przez dalsze komponenty systemu.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
@FunctionalInterface
public interface Normalizer {

    /**
     * Normalizuje podany ciąg znaków według zdefiniowanych reguł.
     *
     * @param text tekst wejściowy do przetworzenia
     * @return znormalizowany ciąg znaków lub pusty ciąg znaków, jeśli wejście było puste lub {@code null}
     */
    String normalize(String text);
}