package pl.j.reinmar.io.builder;

import pl.j.reinmar.io.format.Formatter;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class ReportBuilder {

    public static String basic(TextStats stats, Formatter f) {
        return f.basic(stats);
    }

    public static String full(TextStats stats, Map<String, Integer> freq, Formatter f) {
        return f.full(stats, freq);
    }

    public static String frequency(Map<String, Integer> freq, Formatter f) {
        return f.frequency(freq);
    }

}