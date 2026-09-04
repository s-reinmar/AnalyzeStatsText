package pl.j.reinmar.core;

import pl.j.reinmar.io.FileReader;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;
import pl.j.reinmar.model.WordSort;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TextAnalyzer {

    private final Normalizer normalizer;
    private final Tokenizer tokenizer;
    private final SentenceTokenizer sentenceTokenizer;

    public TextAnalyzer(Normalizer normalizer,
                        Tokenizer tokenizer,
                        SentenceTokenizer sentenceTokenizer) {
        this.normalizer = Objects.requireNonNull(normalizer);
        this.tokenizer = Objects.requireNonNull(tokenizer);
        this.sentenceTokenizer = Objects.requireNonNull(sentenceTokenizer);
    }

    // ===================== STATYSTYKI =====================

    public TextStats analyze(String text) {
        String original = Objects.requireNonNullElse(text, "");

        int charsWithSpaces = original.length();
        int charsWithoutSpaces = original.replaceAll("\\s+", "").length();

        String normalized = normalizer.normalize(original);
        List<String> words = tokenizer.words(normalized);
        List<String> sentences = sentenceTokenizer.sentences(original);

        return new TextStats(charsWithSpaces, charsWithoutSpaces, words.size(), sentences.size());
    }

    public TextStats analyzeFile(String path) throws IOException {
        return analyze(FileReader.readResource(path));
    }

    // ===================== ANALIZA SŁÓW (NOWE API) =====================

    public Object analyzeWords(String text,
                               Set<String> stopWords,
                               int minWordLength,
                               WordSort sortMode,
                               int topN,
                               WordAnalysisMode mode) {

        String normalized = normalizer.normalize(Objects.requireNonNullElse(text, ""));
        List<String> words = tokenizer.words(normalized);

        Map<String, Integer> freq = words.stream()
                .filter(w -> stopWords == null || !stopWords.contains(w))
                .filter(w -> w.length() >= Math.max(1, minWordLength))
                .collect(Collectors.toMap(
                        w -> w,
                        w -> 1,
                        Integer::sum
                ));

        return switch (mode) {

            case FREQUENCY_MAP -> freq;

            case FREQUENCY_SORTED -> freq.entrySet().stream()
                    .map(e -> new WordCount(e.getKey(), e.getValue()))
                    .sorted(sortMode.comparator())
                    .collect(Collectors.toMap(
                            WordCount::word,
                            WordCount::count,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

            case TOP_WORDS -> freq.entrySet().stream()
                    .map(e -> new WordCount(e.getKey(), e.getValue()))
                    .sorted(sortMode.comparator())
                    .limit(Math.max(1, topN))
                    .collect(Collectors.toList());

            case ALL_WORDS_SORTED -> freq.entrySet().stream()
                    .map(e -> new WordCount(e.getKey(), e.getValue()))
                    .sorted(sortMode.comparator())
                    .collect(Collectors.toList());
        };
    }

    public Object analyzeWordsFromFile(String path,
                                       Set<String> stopWords,
                                       int minWordLength,
                                       WordSort sortMode,
                                       int topN,
                                       WordAnalysisMode mode) throws IOException {

        return analyzeWords(
                FileReader.readResource(path),
                stopWords,
                minWordLength,
                sortMode,
                topN,
                mode
        );
    }
}