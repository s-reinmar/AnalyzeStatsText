package pl.j.reinmar.app;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.WordCount;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.UserInput;
import pl.j.reinmar.ui.StatsPrinter;
import pl.j.reinmar.ui.ReportSaver;

import java.util.*;

public class TextMenu {

    private final TextAnalyzer analyzer;
    private final String path;

    private final UserInput input;
    private final StatsPrinter printer;
    private final ReportSaver saver;

    private final Set<String> stopWords = new HashSet<>(List.of(
            "i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u","ten","ta","to",
            "jak","który","która","które","te","dla","przy","albo","lub","czy","tam","tu","nad","pod",
            "od","bez","więc","co","tak","tylko","mnie","ciebie","jego","jej","ich"
    ));

    private int minWordLength = 2;

    public TextMenu(TextAnalyzer analyzer, String path, Scanner sc) {
        this.analyzer = analyzer;
        this.path = path;
        this.input = new UserInput(sc);
        this.printer = new StatsPrinter();
        this.saver = new ReportSaver(analyzer, input);
    }

    // ===================== MENU =====================

    public void run() {
        while (true) {
            printMenu();
            switch (input.readLine().trim()) {
                case "1" -> printer.printBasic(analyzer, path);
                case "2" -> showTopWords();
                case "3" -> printer.printFrequencyFragment(analyzer, path, stopWords, minWordLength);
                case "4" -> minWordLength = input.askMinWordLength(minWordLength);
                case "5" -> toggleStopWords();
                case "6" -> saver.saveBasic(path, stopWords, minWordLength);
                case "7" -> saver.saveFull(path, stopWords, minWordLength);
                case "8" -> saver.saveFrequency(path, stopWords, minWordLength);
                case "0" -> { System.out.println("Koniec. Do zobaczenia!"); return; }
                default -> System.out.println("Nieznana opcja.");
            }
        }
    }


    // ===================== LOGIKA OPCJI =====================

    private void showTopWords() {
        int n = input.askInt("Podaj N", 20);
        WordSort sort = input.askSortMode();
        printer.printTop(analyzer, path, n, stopWords, minWordLength, sort);
    }


    private void toggleStopWords() {
        if (stopWords.isEmpty()) {
            stopWords.addAll(List.of("i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u",
                    "ten","ta","to","jak","który","która","które","te","dla","przy","albo","lub",
                    "czy","tam","tu","nad","pod","od","bez","więc","co","tak","tylko","mnie",
                    "ciebie","jego","jej","ich"));
            System.out.println("Stop‑words: WŁĄCZONE");
        } else {
            stopWords.clear();
            System.out.println("Stop‑words: WYŁĄCZONE");
        }
    }


    private boolean stopWordsEnabled() {
        return !stopWords.isEmpty();
    }

    private void printMenu() {
        System.out.println("""
                
                === MENU ===
                1) Podstawowe statystyki
                2) Top N słów
                3) Fragment częstotliwości
                4) Zmień minWordLength
                5) Włącz/wyłącz stop‑words
                6) Zapisz podstawowe statystyki
                7) Zapisz pełne statystyki
                8) Zapisz częstotliwości słów
                0) Wyjście
                Wybór: """);
    }
}