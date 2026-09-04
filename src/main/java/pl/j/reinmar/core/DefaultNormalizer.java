package pl.j.reinmar.core;

import java.util.Objects;

/**
 * DefaultNormalizer -> domyślne wykorzystanie (trim + usunięcie interpunkcji + lowercase)
 */

public class DefaultNormalizer implements  Normalizer {

    // regex jako stała, aby nie śmiecić w kodzie
    private static final String PUNCT_REGEX = "[\\p{Punct}„”»«]";

    @Override
    public String normalize(String text) {
        String t = Objects.requireNonNullElse(text, "").trim();
        if (t.isEmpty()) return "";
        return t.toLowerCase().replaceAll(PUNCT_REGEX, " ");
    }
}