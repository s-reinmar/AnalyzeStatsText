package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.io.builder.ReportType;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.UserInput;

import java.nio.file.Path;

/**
 * Akcja menu odpowiedzialna za zapisywanie raportu częstotliwości słów do pliku.
 * <p>
 * Klasa pobiera od użytkownika ścieżkę docelową, format pliku wyjściowego oraz tryb sortowania
 * za pomocą komponentu {@link UserInput}, po czym przekazuje skonfigurowane parametry do
 * komponentu {@link ReportSaver} w celu wygenerowania raportu typu {@link ReportType#FREQUENCY}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class SaveFrequencyReportAction implements MenuAction {

    /** Komponent odpowiedzialny za zapisywanie raportu na dysku. */
    private final ReportSaver saver;

    /** Komponent do interakcji z użytkownikiem i pobierania parametrów zapisu. */
    private final UserInput input;

    /** Ścieżka do analizowanego pliku źródłowego. */
    private final String path;

    /** Obiekt ustawień aplikacji z aktywnymi filtrami. */
    private final Settings settings;

    /**
     * Tworzy nową akcję zapisu raportu częstotliwości słów.
     *
     * @param saver    komponent {@link ReportSaver} wykonujący operację zapisu
     * @param input    komponent {@link UserInput} obsługujący zapytania o ścieżkę, format i tryb sortowania
     * @param path     ścieżka do analizowanego pliku źródłowego
     * @param settings obiekt {@link Settings} z konfiguracją filtrowania
     */
    public SaveFrequencyReportAction(ReportSaver saver, UserInput input, String path, Settings settings) {
        this.saver = saver;
        this.input = input;
        this.path = path;
        this.settings = settings;
    }

    /**
     * Wykonuje procedurę pobrania od użytkownika ścieżki wyjściowej, formatu oraz kryterium sortowania,
     * a następnie inicjuje zapis raportu częstotliwości na dysku.
     */
    @Override
    public void execute() {
        Path output = input.askOutputPath("frequency_report.txt");
        ReportWriter.Format format = input.askReportFormat();
        WordSort sort = input.askSortMode();
        saver.saveReport(output, path, ReportType.FREQUENCY, settings.getStopWords(),
                settings.getMinWordLength(), sort, 0, format);
    }

    /**
     * Zwraca etykietę opcji wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji menu
     */
    @Override
    public String label() {
        return "8) Zapisz częstotliwości słów";
    }
}