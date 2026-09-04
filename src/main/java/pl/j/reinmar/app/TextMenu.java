package pl.j.reinmar.app;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;

import java.util.*;

/**
 * TextMenu -> klasa odpowiedzialna za interaktywne menu (switch)
 */

public class TextMenu {
    private final TextAnalyzer analyzer;
    private final String path;
    private final Scanner sc;

    // Domyślnie włączona prosta lista polskich stop-words
    private final Set<String> stopWords = new HashSet<>(Arrays.asList(
            "i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u","ten","ta","to",
            "jak","który","która","które","te","dla","przy","albo","lub","czy","tam","tu","nad","pod",
            "od","bez","więc","co","tak","tylko","mnie","ciebie","jego","jej","ich"
    ));

    private int minWordLength = 2; // ignoruj bardzo krótkie „słowa”

    public TextMenu(TextAnalyzer analyzer, String path, Scanner sc) {
        this.analyzer = analyzer;
        this.path = path;
        this.sc = sc;
    }

    /** Główna pętla menu */
    public void run() {
        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> showBasicStats();
                case "2" -> showTopWords();
                case "3" -> showFrequencyFragment();
                case "4" -> changeMinWordLength();
                case "5" -> toggleStopWords();
                case "0" -> {
                    System.out.println("Koniec. Do zobaczenia!");
                    return;
                }
                default -> System.out.println("Nieznana opcja. Spróbuj ponownie.");
            }
        }
    }

    // funkcje do menu

    private void showBasicStats() {
        try {
            TextStats stats = analyzer.analyzeFile(path);
            System.out.println("=== STATYSTYKI ===");
            System.out.println("Słowa: " + stats.words());
            System.out.println("Znaki (ze spacjami): " + stats.charsWithSpaces());
            System.out.println("Znaki (bez spacji): " + stats.charsWithoutSpaces());
            System.out.println("Zdania: " + stats.sentences());
        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }

    private void showTopWords() {
        System.out.print("Podaj N (ile najczęstszych słów pokazać): ");
        int topN = parsePositiveInt(sc.nextLine(), 20);
        try {
            List<WordCount> top = analyzer.topWordsFromFile(
                    path,
                    topN,
                    stopWordsEnabled() ? stopWords : null,
                    minWordLength
            );
            System.out.println("=== TOP " + topN + " słów ===");
            for (WordCount wc : top) {
                System.out.printf("%-20s : %d%n", wc.word(), wc.count());
            }
        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }

    private void showFrequencyFragment() {
        try {
            Map<String, Integer> freq = analyzer.wordFrequencyFromFile(
                    path,
                    stopWordsEnabled() ? stopWords : null,
                    minWordLength
            );
            // Posortuj: malejąco po liczbie wystąpień, przy remisie alfabetycznie
            List<Map.Entry<String,Integer>> sorted = new ArrayList<>(freq.entrySet());
            sorted.sort(Map.Entry.<String,Integer>comparingByValue().reversed()
                    .thenComparing(Map.Entry::getKey));

            int limit = Math.min(50, sorted.size());
            System.out.println("=== Częstotliwości (pierwsze " + limit + " pozycji) ===");
            for (int i = 0; i < limit; i++) {
                var e = sorted.get(i);
                System.out.printf("%-20s : %d%n", e.getKey(), e.getValue());
            }
            if (sorted.size() > limit) {
                System.out.println("... (razem pozycji: " + sorted.size() + ")");
            }
        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }

    private void changeMinWordLength() {
        System.out.print("Nowa minimalna długość słowa (obecnie " + minWordLength + "): ");
        int val = parsePositiveInt(sc.nextLine(), minWordLength);
        minWordLength = Math.max(1, val);
        System.out.println("Ustawiono minWordLength = " + minWordLength);
    }

    private void toggleStopWords() {
        if (stopWordsEnabled()) {
            stopWords.clear();
            System.out.println("Stop‑words: WYŁĄCZONE");
        } else {
            stopWords.addAll(Arrays.asList(
                    "i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u","ten","ta","to",
                    "jak","który","która","które","te","dla","przy","albo","lub","czy","tam","tu","nad","pod",
                    "od","bez","więc","co","tak","tylko","mnie","ciebie","jego","jej","ich"
            ));
            System.out.println("Stop‑words: WŁĄCZONE");
        }
    }

    // Pomocnicze

    private int parsePositiveInt(String s, int fallback) {
        try {
            int v = Integer.parseInt(s.trim());
            return v > 0 ? v : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }

    private boolean stopWordsEnabled() {
        return !stopWords.isEmpty();
    }

    private void printMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1) Podstawowe statystyki (słowa, znaki, zdania)");
        System.out.println("2) Top N słów (częstotliwości)");
        System.out.println("3) Pełna lista częstotliwości (fragment)");
        System.out.println("4) Zmień próg długości słowa (minWordLength)");
        System.out.println("5) Włącz/wyłącz stop‑words");
        System.out.println("0) Wyjście");
        System.out.print("Wybór: ");
    }
}
