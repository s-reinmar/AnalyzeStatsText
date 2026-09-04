package pl.j.reinmar.core;

import java.util.List;

/**
 * Tokenizer -> odpowiedzialny za rozbicie na słowa (lub zdania)
 */

public interface Tokenizer {
    List<String> words (String normalizedText);
}