package pl.j.reinmar.io.format;

import pl.j.reinmar.io.util.Escape;
import pl.j.reinmar.io.util.SortUtil;
import pl.j.reinmar.io.util.TimeUtil;
import pl.j.reinmar.model.TextStats;

import java.util.Map;

public class CsvFormatter implements Formatter {

    @Override
    public String basic(TextStats s) {
        return """ 
                metric,value 
                words,%d 
                chars_with_spaces,%d 
                chars_without_spaces,%d 
                sentences,%d """.formatted(
                s.words(),
                s.charsWithSpaces(),
                s.charsWithoutSpaces(),
                s.sentences()
        );
    }

    @Override
    public String full(TextStats s, Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder(basic(s));
        sb.append("\nword,count\n");
        for (var e : SortUtil.sorted(freq)) {
            sb.append(Escape.csv(e.getKey())).append(",").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String frequency(Map<String, Integer> freq) {
        StringBuilder sb = new StringBuilder("word,count\n");
        for (var e : SortUtil.sorted(freq)) {
            sb.append(Escape.csv(e.getKey())).append(",").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}