package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.StatsPrinter;

public class FrequencyFragmentAction implements MenuAction {

    private final TextAnalyzer analyzer;
    private final StatsPrinter printer;
    private final String path;
    private final Settings settings;

    public FrequencyFragmentAction(TextAnalyzer analyzer, StatsPrinter printer, String path, Settings settings) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.path = path;
        this.settings = settings;
    }

    @Override
    public void execute() {
        printer.printFrequencyPreview(
                analyzer, path, settings.getStopWords(), settings.getMinWordLength(), WordSort.FREQUENCY_DESC
        );
    }

    @Override
    public String label() {
        return "3) Fragment częstotliwości";
    }
}