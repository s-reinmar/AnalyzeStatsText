package pl.j.reinmar.app.menu;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.actions.*;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.StatsPrinter;
import pl.j.reinmar.ui.UserInput;

import java.util.EnumMap;
import java.util.Map;

public class MenuActionFactory {

    public static Map<MenuOption, MenuAction> create(
            TextAnalyzer analyzer,
            String path,
            UserInput input,
            StatsPrinter printer,
            ReportSaver saver,
            Settings settings
    ) {
        Map<MenuOption, MenuAction> map = new EnumMap<>(MenuOption.class);

        map.put(MenuOption.BASIC_STATS,
                new BasicStatsAction(analyzer, printer, path));

        map.put(MenuOption.TOP_WORDS,
                new TopWordsAction(analyzer, printer, input, path, settings));

        map.put(MenuOption.FREQUENCY_FRAGMENT,
                new FrequencyFragmentAction(analyzer, printer, path, settings));

        map.put(MenuOption.CHANGE_MIN_LENGTH,
                new ChangeMinWordLengthAction(input, settings));

        map.put(MenuOption.TOGGLE_STOPWORDS,
                new ToggleStopWordsAction(settings));

        map.put(MenuOption.SAVE_BASIC,
                new SaveBasicReportAction(saver, input, path, settings));

        map.put(MenuOption.SAVE_FULL,
                new SaveFullReportAction(saver, input, path, settings));

        map.put(MenuOption.SAVE_FREQUENCY,
                new SaveFrequencyReportAction(saver, input, path, settings));

        map.put(MenuOption.EXIT,
                new ExitAction());

        return map;
    }

}