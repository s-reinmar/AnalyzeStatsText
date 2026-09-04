package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.StatsPrinter;
import pl.j.reinmar.ui.UserInput;

public class TopWordsAction implements MenuAction {

    private final TextAnalyzer analyzer;
    private final StatsPrinter printer;
    private final UserInput input;
    private final String path;
    private final Settings settings;

    public TopWordsAction(TextAnalyzer analyzer, StatsPrinter printer, UserInput input,
                          String path, Settings settings) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.input = input;
        this.path = path;
        this.settings = settings;
    }

    @Override
    public void execute() {
        int n = input.askInt("Podaj N", 20);
        WordSort sort = input.askSortMode();
        printer.printTopWords(analyzer, path, n, settings.getStopWords(), settings.getMinWordLength(), sort);
    }

    @Override
    public String label() {
        return "2) Top N słów";
    }
}