package pl.j.reinmar.app;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;

import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.io.ReportWriter.Format; // wewn. enum

import java.nio.file.Path;

import java.util.*; // kolekcje

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
                case "6" -> saveBasicStats(); // add
                case "7" -> saveFullStats(); // add
                case "8" -> saveWordFrequency(); // add
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

    private void saveBasicStats() {
        try {
            TextStats stats = analyzer.analyzeFile(path);
            Format format = askFormat();
            Path out = askOutputPath(defaultName("basic_stats", format));
            ReportWriter.writeBasicStats(stats, out, format);
            System.out.println("Zapisano: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Błąd zapisu: " + e.getMessage());
        }
    }

    private void saveFullStats() {
        try {
            TextStats stats = analyzer.analyzeFile(path);
            Map<String,Integer> freq = analyzer.wordFrequencyFromFile(
                    path, stopWordsEnabled() ? stopWords : null, minWordLength
            );
            Format format = askFormat();
            Path out = askOutputPath(defaultName("full_stats", format));
            ReportWriter.writeFullStats(stats, freq, out, format);
            System.out.println("Zapisano: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Błąd zapisu: " + e.getMessage());
        }
    }

    private void saveWordFrequency() {
        try {
            Map<String,Integer> freq = analyzer.wordFrequencyFromFile(
                    path, stopWordsEnabled() ? stopWords : null, minWordLength
            );
            Format format = askFormat();
            Path out = askOutputPath(defaultName("word_frequency", format));
            ReportWriter.writeWordFrequency(freq, out, format);
            System.out.println("Zapisano: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Błąd zapisu: " + e.getMessage());
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
        System.out.println("6) Zapisz podstawowe statystyki (CSV/TXT/JSON/XML)"); // new
        System.out.println("7) Zapisz pełne statystyki (CSV/TXT/JSON/XML)"); // new
        System.out.println("8) Zapisz częstotliwości słów (CSV/TXT/JSON/XML)"); // new
        System.out.println("0) Wyjście");
        System.out.print("Wybór: ");
    }

    private Format askFormat() {
        System.out.print("Wybierz format (csv/txt/json/xml): ");
        String f = sc.nextLine().trim().toLowerCase(Locale.ROOT);
        return switch (f) {
            case "csv" -> Format.CSV;
            case "txt" -> Format.TXT;
            case "json" -> Format.JSON;
            case "xml" -> Format.XML;
            default -> {
                System.out.println("Nieznany format, domyślnie: JSON");
                yield Format.JSON;
            }
        };
    }

    private java.nio.file.Path askOutputPath(String defaultFileName) {
        System.out.print("Podaj nazwę pliku wyjściowego (ENTER = " + defaultFileName + "): ");
        String name = sc.nextLine().trim();
        String finalName = name.isEmpty() ? defaultFileName : name;
        return java.nio.file.Path.of(finalName);
    }

    private String defaultName(String base, Format f) {
        String ext = switch (f) {
            case CSV -> "csv";
            case TXT -> "txt";
            case JSON -> "json";
            case XML -> "xml";
        };
        // Jeśli ścieżka bazowa to np. 'pan_tadeusz.txt',  utnij '.txt' i dodaj sufiks raportu
        String stem = stripTxtSuffix(path);
        return stem + "-" + base + "." + ext;
    }

    private String stripTxtSuffix(String p) {
        if (p != null && p.toLowerCase(Locale.ROOT).endsWith(".txt")) {
            return p.substring(0, p.length() - 4);
        }
        return p;
    }

}
