package pl.j.reinmar.io.format;

import pl.j.reinmar.model.TextStats;

import java.util.Map;

public interface Formatter {
    String formatBasic(TextStats stats);
    String formatFull(TextStats stats, Map<String,Integer> freq);
    String formatFrequency(Map<String,Integer> freq);

}