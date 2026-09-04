package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.io.util.TimeUtil;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class JsonFormatter implements Formatter {

    @Override
    public String formatBasic(TextStats stats) {
        return """
                {
                  "type": "basic_stats",
                  "generatedAt": "%s",
                  "stats": {
                    "words": %d,
                    "charsWithSpaces": %d,
                    "charsWithoutSpaces": %d,
                    "sentences": %d
                  }
                }
                """.formatted(
                TimeUtil.now(),
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        );
    }

    @Override
    public String formatFull(TextStats stats, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("""
                {
                  "type": "full_stats",
                  "generatedAt": "%s",
                  "stats": {
                    "words": %d,
                    "charsWithSpaces": %d,
                    "charsWithoutSpaces": %d,
                    "sentences": %d
                  },
                  "frequency": {
                """.formatted(
                TimeUtil.now(),
                stats.words(),
                stats.charsWithSpaces(),
                stats.charsWithoutSpaces(),
                stats.sentences()
        ));

        freq.forEach((w, c) ->
                sb.append("    \"%s\": %d,\n".formatted(Escape.json(w), c))
        );

        if (!freq.isEmpty()) sb.setLength(sb.length() - 2); // usuń ostatni przecinek

        sb.append("\n  }\n}");
        return sb.toString();
    }

    @Override
    public String formatFrequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("""
                {
                  "type": "frequency",
                  "generatedAt": "%s",
                  "frequency": {
                """.formatted(TimeUtil.now()));

        freq.forEach((w, c) ->
                sb.append("    \"%s\": %d,\n".formatted(Escape.json(w), c))
        );

        if (!freq.isEmpty()) sb.setLength(sb.length() - 2);

        sb.append("\n  }\n}");
        return sb.toString();
    }
}