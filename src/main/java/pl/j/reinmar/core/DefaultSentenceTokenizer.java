package pl.j.reinmar.core;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultSentenceTokenizer ->
 */

public class DefaultSentenceTokenizer implements SentenceTokenizer {

    @Override
    public List<String> sentences (String text) {
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