package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.StatsPrinter;

/**
 * Akcja menu odpowiedzialna za wyświetlenie podglądu analizy częstotliwości słów.
 * <p>
 * Klasa pobiera z ustawień ({@link Settings}) kryteria filtrowania (stop-words oraz
 * minimalną długość słowa), po czym deleguje wygenerowanie i wydruk fragmentu listy
 * najczęstszych słów do komponentu {@link StatsPrinter}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class FrequencyFragmentAction implements MenuAction {

    /** Analizator tekstu zawierający przetworzone dane. */
    private final TextAnalyzer analyzer;

    /** Komponent odpowiedzialny za drukowanie statystyk w interfejsie użytkownika. */
    private final StatsPrinter printer;

    /** Ścieżka do pliku lub źródła analizowanego tekstu. */
    private final String path;

    /** Obiekt konfiguracji zawierający aktywne filtry i reguły analizy. */
    private final Settings settings;

    /**
     * Tworzy nową akcję wyświetlania fragmentu analizy częstotliwości.
     *
     * @param analyzer instancja {@link TextAnalyzer} przechowująca dane analityczne
     * @param printer  komponent {@link StatsPrinter} do prezentacji wyników
     * @param path     ścieżka do analizowanego pliku źródłowego
     * @param settings obiekt {@link Settings} z konfiguracją filtrowania
     */
    public FrequencyFragmentAction(TextAnalyzer analyzer, StatsPrinter printer, String path, Settings settings) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.path = path;
        this.settings = settings;
    }

    /**
     * Wykonuje akcję poprzez przekazanie parametrów filtrowania z ustawień do drukarki statystyk.
     */
    @Override
    public void execute() {
        printer.printFrequencyPreview(
                analyzer, path, settings.getStopWords(), settings.getMinWordLength(), WordSort.FREQUENCY_DESC
        );
    }

    /**
     * Zwraca etykietę opcji wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji menu
     */
    @Override
    public String label() {
        return "3) Fragment częstotliwości";
    }
}