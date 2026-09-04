package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.SortUtil;
import pl.j.reinmar.io.util.TimeUtil;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class TxtFormatter implements Formatter {

    private static final String NL = System.lineSeparator();

    @Override
    public String basic(TextStats s) {
        return "=== Podstawowe statystyki ===" + NL +
                "Wygenerowano: " + TimeUtil.now() + NL +
                "Słowa: " + s.words() + NL +
                "Znaki (ze spacjami): " + s.charsWithSpaces() + NL +
                "Znaki (bez spacji): " + s.charsWithoutSpaces() + NL +
                "Zdania: " + s.sentences() + NL;
    }

    @Override
    public String full(TextStats s, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append(basic(s)).append(NL);
        sb.append("=== Częstotliwość słów ===").append(NL);

        for (var e : SortUtil.sorted(freq)) {
            sb.append(String.format("%-20s : %d", e.getKey(), e.getValue()))
                    .append(NL);
        }
        return sb.toString();
    }

    @Override
    public String frequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Częstotliwość słów ===").append(NL);

        for (var e : SortUtil.sorted(freq)) {
            sb.append(String.format("%-20s : %d", e.getKey(), e.getValue()))
                    .append(NL);
        }
        return sb.toString();
    }
}