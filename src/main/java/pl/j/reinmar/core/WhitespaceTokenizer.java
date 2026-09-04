package pl.j.reinmar.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WhitespaceTokenizer -> najprostsze dzielenie po białych znakach
 */

public class WhitespaceTokenizer implements Tokenizer {

    private static final String WHITESPACE_REGEX = "\\s+"; //regex dla białych znaków

    @Override
    public List<String> words(String normalizedText) {
        if (normalizedText == null || normalizedText.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] parts = normalizedText.trim().split(WHITESPACE_REGEX);
        return new ArrayList<>(Arrays.asList(parts));
    }
}