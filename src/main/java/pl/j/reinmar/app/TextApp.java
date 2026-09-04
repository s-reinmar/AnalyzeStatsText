package pl.j.reinmar.app;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.app.menu.MenuActionFactory;
import pl.j.reinmar.app.menu.MenuOption;
import pl.j.reinmar.core.DefaultNormalizer;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.core.WhitespaceTokenizer;
import pl.j.reinmar.core.DefaultSentenceTokenizer;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.StatsPrinter;
import pl.j.reinmar.ui.UserInput;

import java.util.*;

public class TextApp {

    public static void main(String[] args) {

        TextAnalyzer analyzer = new TextAnalyzer(
                new DefaultNormalizer(),
                new WhitespaceTokenizer(),
                new DefaultSentenceTokenizer()
        );

        Scanner sc = new Scanner(System.in);
        UserInput input = new UserInput(sc);

        System.out.print("Podaj bazową nazwę pliku (bez .txt): ");
        String baseName = input.readLine().trim();
        String path = baseName + ".txt";

        StatsPrinter printer = new StatsPrinter();
        ReportSaver saver = new ReportSaver(analyzer);

        Settings settings = new Settings();

        Map<MenuOption, MenuAction> actions = MenuActionFactory.create(
                analyzer,
                path,
                input,
                printer,
                saver,
                settings
        );



        TextMenu menu = new TextMenu(input, actions);
        menu.run();
    }
}