package pl.j.reinmar.io;

import pl.j.reinmar.model.TextStats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*; // ArrayList, Collections, List, Map
import java.util.stream.Collectors;


/**
 * ReportWritter -> zapis raportów w formatach CSV, TXT, JSON i XML.
 */

public class ReportWriter {

    /** Format zapisu */
    public enum Format { CSV, TXT, JSON, XML }

    // domyślny konstruktor
    private ReportWriter() {}

    // ======= API publiczne =======

    /* Zapis podstawowych statystyk do pliku w wybranym formacie */
    public static void writeBasicStats (TextStats stats,
                                        Path out,
                                        Format format) throws IOException {
        String content = switch(format) {
            case CSV -> buildBasicCsv(stats);
            case TXT -> buildBasicTxt(stats);
            case JSON -> buildBasicJson(stats);
            case XML -> buildBasicXml(stats);
        };
        write(out, content);
    }

    public static void writeFullStats (TextStats stats,
                                       Map<String, Integer>
                                               freq, Path out,
                                       Format format) throws IOException {
        String content = switch (format) {
            case CSV -> buildFullCsv(stats, freq);
            case TXT -> buildFullTxt(stats, freq);
            case JSON -> buildFullJson(stats, freq);
            case XML -> buildFullXml(stats, freq);
        };
        write(out, content);
    }

    /** Zapis samej częstotliwości słów do pliku w wybranym formacie */
    public static void writeWordFrequency (Map<String, Integer> freq,
                                           Path out,
                                           Format format) throws IOException {
        String content = switch(format) {
            case CSV -> buildFreqCsv(freq);
            case TXT -> buildFreqTxt(freq);
            case JSON -> buildFreqJson(freq);
            case XML -> buildFreqXml(freq);
        };
        write(out, content);
    }

    // ======= Budowanie treści =======

    // --- Basic ---
    private static String buildBasicCsv(TextStats s) {
        // Nagłówki + wartości w jednej linii
        return String.join("\n",
                "metric,value",
                "words," + s.words(),
                "chars_with_spaces," + s.charsWithSpaces(),
                "chars_without_spaces," + s.charsWithoutSpaces(),
                "sentences," + s.sentences()
        ) + "\n";
    }

    private static String buildBasicTxt(TextStats s) {
        String nl = System.lineSeparator();
        return "=== Podstawowe statystyki ===" + nl +
                "Słowa: " + s.words() + nl +
                "Znaki (ze spacjami): " + s.charsWithSpaces() + nl +
                "Znaki (bez spacji): " + s.charsWithoutSpaces() + nl +
                "Zdania: " + s.sentences() + nl;
    }

    private static String buildBasicJson(TextStats s) {
        // Prosty JSON składany ręcznie (bez bibliotek)
        return "{\n" +
                "  \"type\": \"basic_stats\",\n" +
                "  \"generatedAt\": \"" + isoNow() + "\",\n" +
                "  \"stats\": {\n" +
                "    \"words\": " + s.words() + ",\n" +
                "    \"charsWithSpaces\": " + s.charsWithSpaces() + ",\n" +
                "    \"charsWithoutSpaces\": " + s.charsWithoutSpaces() + ",\n" +
                "    \"sentences\": " + s.sentences() + "\n" +
                "  }\n" +
                "}\n";
    }


    private static String buildBasicXml(TextStats s) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report type=\"basic_stats\" generatedAt=\"" + xmlEscape(isoNow()) + "\">\n" +
                "  <stats>\n" +
                "    <words>" + s.words() + "</words>\n" +
                "    <charsWithSpaces>" + s.charsWithSpaces() + "</charsWithSpaces>\n" +
                "    <charsWithoutSpaces>" + s.charsWithoutSpaces() + "</charsWithoutSpaces>\n" +
                "    <sentences>" + s.sentences() + "</sentences>\n" +
                "  </stats>\n" +
                "</report>\n";
    }

// --- FULL ---

    private static String buildFullCsv(TextStats s, Map<String,Integer> freq) {
        // Sekcja statystyk + sekcja częstotliwości
        StringBuilder sb = new StringBuilder();
        sb.append(buildBasicCsv(s));
        sb.append("\nword,count\n");
        for (var e : sortedFreq(freq)) {
            sb.append(csvEscape(e.getKey())).append(",").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }

    private static String buildFullTxt(TextStats s, Map<String,Integer> freq) {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append(buildBasicTxt(s)).append(nl);
        sb.append("=== Częstotliwość słów ===").append(nl);
        for (var e : sortedFreq(freq)) {
            sb.append(String.format("%-20s : %d", e.getKey(), e.getValue())).append(nl);
        }
        return sb.toString();
    }

    private static String buildFullJson(TextStats s, Map<String,Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"type\": \"full_stats\",\n");
        sb.append("  \"generatedAt\": \"").append(isoNow()).append("\",\n");
        sb.append("  \"stats\": {\n");
        sb.append("    \"words\": ").append(s.words()).append(",\n");
        sb.append("    \"charsWithSpaces\": ").append(s.charsWithSpaces()).append(",\n");
        sb.append("    \"charsWithoutSpaces\": ").append(s.charsWithoutSpaces()).append(",\n");
        sb.append("    \"sentences\": ").append(s.sentences()).append("\n");
        sb.append("  },\n");
        sb.append("  \"frequency\": {\n");
        // wpisy „key: value” z przecinkami – ostrożnie z ostatnim
        List<Map.Entry<String,Integer>> entries = new ArrayList<>(sortedFreq(freq));
        for (int i = 0; i < entries.size(); i++) {
            var e = entries.get(i);
            sb.append("    \"").append(jsonEscape(e.getKey())).append("\": ").append(e.getValue());
            sb.append(i < entries.size() - 1 ? ",\n" : "\n");
        }
        sb.append("  }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private static String buildFullXml(TextStats s, Map<String,Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<report type=\"full_stats\" generatedAt=\"").append(xmlEscape(isoNow())).append("\">\n");
        sb.append("  <stats>\n");
        sb.append("    <words>").append(s.words()).append("</words>\n");
        sb.append("    <charsWithSpaces>").append(s.charsWithSpaces()).append("</charsWithSpaces>\n");
        sb.append("    <charsWithoutSpaces>").append(s.charsWithoutSpaces()).append("</charsWithoutSpaces>\n");
        sb.append("    <sentences>").append(s.sentences()).append("</sentences>\n");
        sb.append("  </stats>\n");
        sb.append("  <frequency>\n");
        for (var e : sortedFreq(freq)) {
            sb.append("    <item word=\"").append(xmlEscape(e.getKey())).append("\" count=\"")
                    .append(e.getValue()).append("\"/>\n");
        }
        sb.append("  </frequency>\n");
        sb.append("</report>\n");
        return sb.toString();
    }

    // --- FREQ only ---

    private static String buildFreqCsv(Map<String,Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("word,count\n");
        for (var e : sortedFreq(freq)) {
            sb.append(csvEscape(e.getKey())).append(",").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }

    private static String buildFreqTxt(Map<String,Integer> freq) {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder("=== Częstotliwość słów ===").append(nl);
        for (var e : sortedFreq(freq)) {
            sb.append(String.format("%-20s : %d", e.getKey(), e.getValue())).append(nl);
        }
        return sb.toString();
    }

    private static String buildFreqJson(Map<String,Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"type\": \"word_frequency\",\n");
        sb.append("  \"generatedAt\": \"").append(isoNow()).append("\",\n");
        sb.append("  \"frequency\": {\n");
        List<Map.Entry<String,Integer>> entries = new ArrayList<>(sortedFreq(freq));
        for (int i = 0; i < entries.size(); i++) {
            var e = entries.get(i);
            sb.append("    \"").append(jsonEscape(e.getKey())).append("\": ").append(e.getValue());
            sb.append(i < entries.size() - 1 ? ",\n" : "\n");
        }
        sb.append("  }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private static String buildFreqXml(Map<String,Integer> freq) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<report type=\"word_frequency\" generatedAt=\"").append(xmlEscape(isoNow())).append("\">\n");
        sb.append("  <frequency>\n");
        for (var e : sortedFreq(freq)) {
            sb.append("    <item word=\"").append(xmlEscape(e.getKey())).append("\" count=\"")
                    .append(e.getValue()).append("\"/>\n");
        }
        sb.append("  </frequency>\n");
        sb.append("</report>\n");
        return sb.toString();
    }

    // ======= Pomocnicze =======

    private static void write(Path out, String content) throws IOException {
        Files.createDirectories(out.getParent() == null ? Path.of(".") : out.getParent());
        try (var w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            w.write(content);
        }
    }

    private static String isoNow() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private static List<Map.Entry<String,Integer>> sortedFreq(Map<String,Integer> freq) {
        if (freq == null || freq.isEmpty()) return Collections.emptyList();
        return freq.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .collect(Collectors.toList());
    }

    // Escaping dla CSV (proste) – jeśli słowo zawiera cudzysłów lub przecinek, owiń w cudzysłowy i zdubelkuj cudzysłów
    private static String csvEscape(String s) {
        if (s == null) return "";
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String escaped = s.replace("\"", "\"\"");
        return needQuotes ? "\"" + escaped + "\"" : escaped;
    }

    // Escaping dla JSON
    private static String jsonEscape(String s) {
        if (s == null) return "";
        StringBuilder b = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  b.append("\\\""); break;
                case '\\': b.append("\\\\"); break;
                case '\n': b.append("\\n");  break;
                case '\r': b.append("\\r");  break;
                case '\t': b.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        b.append(String.format("\\u%04x", (int)c));
                    } else {
                        b.append(c);
                    }
            }
        }
        return b.toString();
    }

    // Escaping dla XML
    private static String xmlEscape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}