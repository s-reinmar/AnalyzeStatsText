package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.io.builder.ReportType;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.UserInput;

import java.nio.file.Path;
import java.util.Set;

public class SaveFullReportAction implements MenuAction {

    private final ReportSaver saver;
    private final UserInput input;
    private final String path;
    private final Set<String> stopWords;
    private final int[] minWordLengthRef;

    public SaveFullReportAction(ReportSaver saver, UserInput input, String path,
                                Set<String> stopWords, int[] minWordLengthRef) {
        this.saver = saver;
        this.input = input;
        this.path = path;
        this.stopWords = stopWords;
        this.minWordLengthRef = minWordLengthRef;
    }

    @Override
    public void execute() {
        Path output = input.askOutputPath("full_report.txt");
        ReportWriter.Format format = input.askReportFormat();
        WordSort sort = input.askSortMode();
        saver.saveReport(output, path, ReportType.FULL, stopWords, minWordLengthRef[0],
                sort, 0, format);
    }

    @Override
    public String label() {
        return "7) Zapisz pełne statystyki";
    }
}