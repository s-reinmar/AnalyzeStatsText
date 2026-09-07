package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.ui.StatsPrinter;

/**
 * Akcja menu odpowiedzialna za wywołanie i wyświetlenie podstawowych statystyk tekstu.
 * <p>
 * Klasa implementuje wzorzec Polecenia ({@link MenuAction}) i deleguje zadanie
 * wyrenderowania wyników do komponentu {@link StatsPrinter} na podstawie
 * analizy wykonanej przez {@link TextAnalyzer}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class BasicStatsAction implements MenuAction {

    /** Analizator tekstu zawierający przetworzone dane. */
    private final TextAnalyzer analyzer;

    /** Komponent odpowiedzialny za drukowanie statystyk w interfejsie użytkownika. */
    private final StatsPrinter printer;

    /** Ścieżka do pliku lub źródła analizowanego tekstu. */
    private final String path;

    /**
     * Tworzy nową akcję wyświetlania podstawowych statystyk.
     *
     * @param analyzer instancja {@link TextAnalyzer} przechowująca dane analityczne
     * @param printer  komponent {@link StatsPrinter} do prezentacji wyników
     * @param path     ścieżka do analizowanego pliku źródłowego
     */
    public BasicStatsAction(TextAnalyzer analyzer, StatsPrinter printer, String path) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.path = path;
    }

    /**
     * Wykonuje akcję poprzez przekazanie analizatora i ścieżki do drukarki statystyk.
     */
    @Override
    public void execute() {
        printer.printBasicStats(analyzer, path);
    }

    /**
     * Zwraca etykietę opcji wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji menu
     */
    @Override
    public String label() {
        return "1) Podstawowe statystyki";
    }
}