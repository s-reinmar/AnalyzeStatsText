package pl.j.reinmar.ui;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.model.TextStats;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ReportSaver {

    private final TextAnalyzer analyzer;
    private final UserInput input;

    public ReportSaver(TextAnalyzer analyzer, UserInput input) {
        this.analyzer = analyzer;
        this.input = input;
    }

    // ===================== PUBLICZNE METODY ZAPISU =====================

    public void saveBasic(String path, Set<String> stopWords, int minWordLength) {
        ReportWriter.Format format = input.askFormat();
        Path out = input.askOutputPath(defaultName(path, "basic_stats", format));

        saveReport(out, () -> {
            TextStats stats = analyzer.analyzeFile(path);
            ReportWriter.writeBasicStats(stats, out, format);
        });
    }

    public void saveFull(String path, Set<String> stopWords, int minWordLength) {
        ReportWriter.Format format = input.askFormat();
        Path out = input.askOutputPath(defaultName(path, "full_stats", format));

        saveReport(out, () -> {
            TextStats stats = analyzer.analyzeFile(path);
            Map<String,Integer> freq = analyzer.wordFrequencyFromFile(
                    path,
                    stopWords != null && !stopWords.isEmpty() ? stopWords : null,
                    minWordLength
            );
            ReportWriter.writeFullStats(stats, freq, out, format);
        });
    }

    public void saveFrequency(String path, Set<String> stopWords, int minWordLength) {
        ReportWriter.Format format = input.askFormat();
        Path out = input.askOutputPath(defaultName(path, "word_frequency", format));

        saveReport(out, () -> {
            Map<String,Integer> freq = analyzer.wordFrequencyFromFile(
                    path,
                    stopWords != null && !stopWords.isEmpty() ? stopWords : null,
                    minWordLength
            );
            ReportWriter.writeWordFrequency(freq, out, format);
        });
    }

    // ===================== WSPÓLNY MECHANIZM ZAPISU =====================

    @FunctionalInterface
    private interface IOAction {
        void run() throws Exception;
    }

    private void saveReport(Path out, IOAction action) {
        try {
            action.run();
            System.out.println("Zapisano: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Błąd zapisu: " + e.getMessage());
        }
    }

    // ===================== GENEROWANIE NAZW PLIKÓW =====================

    private String defaultName(String inputPath, String base, ReportWriter.Format f) {
        String ext = switch (f) {
            case CSV -> "csv";
            case TXT -> "txt";
            case JSON -> "json";
            case XML -> "xml";
        };

        String stem = stripTxtSuffix(inputPath);
        return stem + "-" + base + "." + ext;
    }

    private String stripTxtSuffix(String p) {
        if (p != null && p.toLowerCase(Locale.ROOT).endsWith(".txt")) {
            return p.substring(0, p.length() - 4);
        }
        return p;
    }
}