package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class CsvFormatter implements Formatter {

    @Override
    public String formatBasic(TextStats stats) {
        return "metric,value\n" +
                "words," + stats.words() + "\n" +
                "chars_with_spaces," + stats.charsWithSpaces() + "\n" +
                "chars_without_spaces," + stats.charsWithoutSpaces() + "\n" +
                "sentences," + stats.sentences() + "\n";
    }

    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder(formatBasic(stats));
        sb.append("\nword,count\n");
        freq.forEach((w, c) -> sb.append(Escape.csv(w)).append(",").append(c).append("\n"));
        return sb.toString();
    }

    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("word,count\n");
        freq.forEach((w, c) -> sb.append(Escape.csv(w)).append(",").append(c).append("\n"));
        return sb.toString();
    }
}