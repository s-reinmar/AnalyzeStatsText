package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;

/**
 * Akcja menu odpowiedzialna za przełączanie stanu filtrowania słów ignorowanych (stop-words).
 * <p>
 * Klasa wywołuje metodę {@link Settings#toggleStopWords()}, która w zależności od aktualnego stanu
 * włącza (zaludnia słownikiem) lub wyłącza (czyści) listę słów ignorowanych podczas analizy.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class ToggleStopWordsAction implements MenuAction {

    /** Obiekt globalnych ustawień aplikacji. */
    private final Settings settings;

    /**
     * Tworzy nową akcję przełączania stanu listy stop-words.
     *
     * @param settings obiekt {@link Settings} przechowujący konfigurację filtrowania
     */
    public ToggleStopWordsAction(Settings settings) {
        this.settings = settings;
    }

    /**
     * Wykonuje procedurę włączenia lub wyłączenia słów ignorowanych w konfiguracji.
     */
    @Override
    public void execute() {
        settings.toggleStopWords();
    }

    /**
     * Zwraca etykietę opcji wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji menu
     */
    @Override
    public String label() {
        return "5) Włącz/wyłącz stop‑words";
    }
}