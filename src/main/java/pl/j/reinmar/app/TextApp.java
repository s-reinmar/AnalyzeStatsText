package pl.j.reinmar.app;

import pl.j.reinmar.io.FileReader;

import static pl.j.reinmar.app.TextMethod.*;

public class TextApp {

    public static void main(String[] args) {

        // wykorzystanie funkcji czytania z plików zasobów Resource
        String text = FileReader.readResource("file.txt");


        System.out.println("=== Zawartość pliku ===");

        System.out.println("Słowa: " + countWords(text));
        System.out.println("Znaki (ze spacjami): " + countCharsWithSpaces(text));
        System.out.println("Znaki (bez spacji): " + countCharsWithoutSpaces(text));

    }
}