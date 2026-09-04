package pl.j.reinmar.io;

import pl.j.reinmar.io.builder.ReportBuilder;
import pl.j.reinmar.io.format.*;
import pl.j.reinmar.io.format.Formatter;
import pl.j.reinmar.model.TextStats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*; // ArrayList, Collections, List, Map
import java.util.stream.Collectors;


public class ReportWriter {

    public enum Format {CSV, TXT, JSON, XML}

    private ReportWriter() {
    }

    // ======= API publiczne =======

    /* Zapis podstawowych statystyk do pliku w wybranym formacie */
    public static void writeBasicStats(TextStats stats, Path out, Format format) throws IOException {
        write(out, ReportBuilder.basic(stats, formatter(format)));
    }

    public static void writeFullStats(TextStats stats, java.util.Map<String, Integer> freq, Path out, Format format) throws IOException {
        write(out, ReportBuilder.full(stats, freq, formatter(format)));
    }

    public static void writeWordFrequency(java.util.Map<String, Integer> freq, Path out, Format format) throws IOException {
        write(out, ReportBuilder.frequency(freq, formatter(format)));
    }

    private static void write(Path out, String content) throws IOException {
        Path parent = out.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(out, content, StandardCharsets.UTF_8);
    }


    private static Formatter formatter(Format f) {
        return switch (f) {
            case CSV -> new CsvFormatter();
            case TXT -> new TxtFormatter();
            case JSON -> new JsonFormatter();
            case XML -> new XmlFormatter();
        };
    }
}