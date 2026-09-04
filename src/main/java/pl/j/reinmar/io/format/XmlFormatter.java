package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.io.util.SortUtil;
import pl.j.reinmar.io.util.TimeUtil;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class XmlFormatter implements Formatter {

    @Override
    public String basic(TextStats s) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <report type="basic_stats" generatedAt="%s">
                  <stats>
                    <words>%d</words>
                    <charsWithSpaces>%d</charsWithSpaces>
                    <charsWithoutSpaces>%d</charsWithoutSpaces>
                    <sentences>%d</sentences>
                  </stats>
                </report>
                """.formatted(
                Escape.xml(TimeUtil.now()),
                s.words(),
                s.charsWithSpaces(),
                s.charsWithoutSpaces(),
                s.sentences()
        );
    }

    @Override
    public String full(TextStats s, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<report type=\"full_stats\" generatedAt=\"")
                .append(Escape.xml(TimeUtil.now()))
                .append("\">\n");

        sb.append("  <stats>\n");
        sb.append("    <words>").append(s.words()).append("</words>\n");
        sb.append("    <charsWithSpaces>").append(s.charsWithSpaces()).append("</charsWithSpaces>\n");
        sb.append("    <charsWithoutSpaces>").append(s.charsWithoutSpaces()).append("</charsWithoutSpaces>\n");
        sb.append("    <sentences>").append(s.sentences()).append("</sentences>\n");
        sb.append("  </stats>\n");

        sb.append("  <frequency>\n");
        for (var e : SortUtil.sorted(freq)) {
            sb.append("    <item word=\"")
                    .append(Escape.xml(e.getKey()))
                    .append("\" count=\"")
                    .append(e.getValue())
                    .append("\"/>\n");
        }
        sb.append("  </frequency>\n");

        sb.append("</report>\n");
        return sb.toString();
    }

    @Override
    public String frequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<report type=\"word_frequency\" generatedAt=\"")
                .append(Escape.xml(TimeUtil.now()))
                .append("\">\n");

        sb.append("  <frequency>\n");
        for (var e : SortUtil.sorted(freq)) {
            sb.append("    <item word=\"")
                    .append(Escape.xml(e.getKey()))
                    .append("\" count=\"")
                    .append(e.getValue())
                    .append("\"/>\n");
        }
        sb.append("  </frequency>\n");

        sb.append("</report>\n");
        return sb.toString();
    }
}