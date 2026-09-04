package pl.j.reinmar.io.format;

import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class TxtFormatter implements Formatter {

    @Override
    public String formatBasic(TextStats stats) {
        return """
                === STATYSTYKI ===
                Słowa: %d
                Znaki (ze spacjami): %d
                Znaki (bez spacji): %d
                Zdania: %d
                """.formatted(
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        );
    }

    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder(formatBasic(stats));
        sb.append("\n=== CZĘSTOTLIWOŚCI ===\n");
        freq.forEach((w, c) -> sb.append("%-20s : %d%n".formatted(w, c)));
        return sb.toString();
    }

    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("=== CZĘSTOTLIWOŚCI ===\n");
        freq.forEach((w, c) -> sb.append("%-20s : %d%n".formatted(w, c)));
        return sb.toString();
    }
}