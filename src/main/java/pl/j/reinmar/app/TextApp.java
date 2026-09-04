package pl.j.reinmar.app;

import pl.j.reinmar.core.*;
import pl.j.reinmar.model.*;

import java.util.*;

public class TextApp {
    public static void main(String[] args) {

        // jeśli implementacje są publiczne:
        TextAnalyzer analyzer = new TextAnalyzer(
                new DefaultNormalizer(),
                new WhitespaceTokenizer(),
                new DefaultSentenceTokenizer());

        Scanner sc = new Scanner(System.in);

        // Pobierz bazową nazwę pliku i zbuduj ścieżkę
        System.out.print("Podaj bazową nazwę pliku (bez .txt): ");
        String baseName = sc.nextLine().trim();
        String path = baseName + ".txt";

        // uruchom interaktywne menu
        new TextMenu (analyzer, path, sc).run();
    }
}