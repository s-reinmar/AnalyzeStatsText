package pl.j.reinmar.io.util;

import java.util.*;
import java.util.stream.Collectors;

public class SortUtil {

    /**
     * Zwraca listę wpisów mapy posortowaną według:
     * <ol>
     *     <li>malejącej liczby wystąpień,</li>
     *     <li>alfabetycznie po słowie (w przypadku remisu).</li>
     * </ol>
     *
     * <p>Jeśli mapa jest pusta lub null, zwracana jest pusta lista.</p>
     *
     * @param freq mapa słowo → liczba wystąpień
     * @return posortowana lista wpisów mapy
     */
    public static List<Map.Entry<String,Integer>> sorted(Map<String,Integer> freq) {
        if (freq == null || freq.isEmpty()) return List.of();
        return freq.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .collect(Collectors.toList());
    }
}