package pl.j.reinmar.core;

/**
 * Normalizer -> odpowiada za przygotowanie tekstu (np. usunięcie interpunkcji, trim, lowercase)
 */

public interface Normalizer {
    String normalize(String text);
}