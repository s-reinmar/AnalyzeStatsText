package pl.j.reinmar.app;

import static pl.j.reinmar.app.TextMethod.*;

public class TextApp {

    public static void main(String[] args) {
        // Przykładowy tekst — później możesz podawać go z args lub Scannerem
        String text = "Ala,          ma kota! A               kot ma   Alę.\nTo jest test.";
        System.out.println("Tekst: " + text);
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Słowa: " + countWords(text));
        System.out.println("Znaki (ze spacjami): " + countCharsWithSpaces(text));
        System.out.println("Znaki (bez spacji): " + countCharsWithoutSpaces(text));
    }
}