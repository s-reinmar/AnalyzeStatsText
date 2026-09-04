package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.StatsPrinter;

import java.util.Set;

public class FrequencyFragmentAction implements MenuAction {

    private final TextAnalyzer analyzer;
    private final StatsPrinter printer;
    private final String path;
    private final Set<String> stopWords;
    private final int minWordLength;

    public FrequencyFragmentAction(TextAnalyzer analyzer, StatsPrinter printer,
                                   String path, Set<String> stopWords, int minWordLength) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.path = path;
        this.stopWords = stopWords;
        this.minWordLength = minWordLength;
    }

    @Override
    public void execute() {
        printer.printFrequencyPreview(
                analyzer, path, stopWords, minWordLength, WordSort.FREQUENCY_DESC
        );
    }

    @Override
    public String label() {
        return "3) Fragment częstotliwości";
    }
}