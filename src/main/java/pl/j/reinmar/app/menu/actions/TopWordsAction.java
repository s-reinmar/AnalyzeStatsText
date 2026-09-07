package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.StatsPrinter;
import pl.j.reinmar.ui.UserInput;

/**
 * Akcja menu odpowiedzialna za wyświetlenie zestawienia Top N najczęstszych słów.
 * <p>
 * Klasa pobiera od użytkownika liczbę elementów do wyświetlenia (N) oraz kryterium sortowania
 * za pomocą komponentu {@link UserInput}, po czym deleguje wyrenderowanie wyników do
 * komponentu {@link StatsPrinter} z uwzględnieniem filtrowania z ustawień ({@link Settings}).
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class TopWordsAction implements MenuAction {

    /** Analizator tekstu zawierający przetworzone dane. */
    private final TextAnalyzer analyzer;

    /** Komponent odpowiedzialny za drukowanie statystyk w interfejsie użytkownika. */
    private final StatsPrinter printer;

    /** Komponent do interakcji z użytkownikiem i pobierania parametrów (liczby N oraz trybu sortowania). */
    private final UserInput input;

    /** Ścieżka do pliku lub źródła analizowanego tekstu. */
    private final String path;

    /** Obiekt konfiguracji zawierający aktywne filtry i reguły analizy. */
    private final Settings settings;

    /**
     * Tworzy nową akcję wyświetlania listy Top N słów.
     *
     * @param analyzer instancja {@link TextAnalyzer} przechowująca dane analityczne
     * @param printer  komponent {@link StatsPrinter} do prezentacji wyników
     * @param input    komponent {@link UserInput} do pobierania parametru N oraz kryterium sortowania
     * @param path     ścieżka do analizowanego pliku źródłowego
     * @param settings obiekt {@link Settings} z konfiguracją filtrowania
     */
    public TopWordsAction(TextAnalyzer analyzer, StatsPrinter printer, UserInput input,
                          String path, Settings settings) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.input = input;
        this.path = path;
        this.settings = settings;
    }

    /**
     * Wykonuje procedurę pobrania liczby N oraz kryterium sortowania od użytkownika,
     * a następnie inicjuje wydruk zestawienia najczęstszych słów.
     */
    @Override
    public void execute() {
        int n = input.askInt("Podaj N", 20);
        WordSort sort = input.askSortMode();
        printer.printTopWords(analyzer, path, n, settings.getStopWords(), settings.getMinWordLength(), sort);
    }

    /**
     * Zwraca etykietę opcji wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji menu
     */
    @Override
    public String label() {
        return "2) Top N słów";
    }
}