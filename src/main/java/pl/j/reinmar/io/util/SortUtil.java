package pl.j.reinmar.io.util;

import java.util.*;
import java.util.stream.Collectors;

public class SortUtil {

    public static List<Map.Entry<String,Integer>> sorted(Map<String,Integer> freq) {
        if (freq == null || freq.isEmpty()) return List.of();
        return freq.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .collect(Collectors.toList());
    }
}