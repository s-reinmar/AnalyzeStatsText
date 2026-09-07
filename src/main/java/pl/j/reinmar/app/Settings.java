package pl.j.reinmar.app;

import pl.j.reinmar.model.WordSort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * Klasa przechowująca ustawienia aplikacji.
 *  <p>
 *  Ustawienia obejmują listę słów wykluczonych (stop‑words), minimalną długość słowa do analizy
 *  oraz domyślny sposób sortowania wyników.
 *  </p>
 *
 *  @author Sławek Reinmar
 *  @version 1.0
 */
public class Settings {

    /**
     * Zbiór ignorowanych słów (tzw. stop-words), które są pomijane podczas analizy tekstu.
     * Domyślnie inicjalizowany podstawowym zestawem spójników i zaimków w języku polskim.
     */
    private final Set<String> stopWords = new HashSet<>(List.of(
            "i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u","ten","ta","to",
            "jak","który","która","które","te","dla","przy","albo","lub","czy","tam","tu","nad","pod",
            "od","bez","więc","co","tak","tylko","mnie","ciebie","jego","jej","ich"
    ));

    /**
     * Minimalna długość słowa uwzględnianego w analizie (włączając podaną wartość).
     * Domyślna wartość to {@code 2}.
     */
    private int minWordLength = 2;

    /**
     * Domyślny sposób sortowania przetworzonych słów.
     * Domyślna wartość to {@link WordSort#FREQUENCY_DESC}.
     */
    private WordSort defaultSort = WordSort.FREQUENCY_DESC;

    /**
     * Tworzy nową instancję ustawień z domyślną konfiguracją.
     */
    public Settings() {
    }

    /**
     * Zwraca aktualny zbiór słów ignorowanych (stop-words).
     *
     * @return zbiór zawierający ignorowane słowa
     */
    public Set<String> getStopWords() {
        return stopWords;
    }

    /**
     * Zwraca ustawioną minimalną długość słowa.
     *
     * @return minimalna długość słowa
     */
    public int getMinWordLength() {
        return minWordLength;
    }

    /**
     * Ustawia minimalną długość słowa.
     * <p>
     * Wartość jest automatycznie korygowana, aby wynosiła co najmniej {@code 1}.
     * </p>
     *
     * @param value nowa minimalna długość słowa
     */
    public void setMinWordLength(int value) {
        this.minWordLength = Math.max(1, value);
    }

    /**
     * Zwraca domyślny sposób sortowania wyników.
     *
     * @return wybrany kryterium sortowania {@link WordSort}
     */
    public WordSort getDefaultSort() {
        return defaultSort;
    }

    /**
     * Ustawia domyślny sposób sortowania wyników.
     *
     * @param sort nowe kryterium sortowania {@link WordSort}
     */
    public void setDefaultSort(WordSort sort) {
        this.defaultSort = sort;
    }

    /**
     * Przełącza stan listy słów ignorowanych (stop-words).
     * <p>
     * Jeśli lista jest pusta, zostaje ponownie zaludniona domyślnym zestawem polskich słów.
     * W przeciwnym razie lista jest czyszczona.
     * Informacja o stanie drukowana jest na standardowe wyjście (konsolę).
     * </p>
     */
    public void toggleStopWords() {
        if (stopWords.isEmpty()) {
            stopWords.addAll(List.of(
                    "i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u",
                    "ten","ta","to","jak","który","która","które","te","dla","przy","albo","lub",
                    "czy","tam","tu","nad","pod","od","bez","więc","co","tak","tylko","mnie",
                    "ciebie","jego","jej","ich"
            ));
            System.out.println("Stop‑words: WŁĄCZONE");
        } else {
            stopWords.clear();
            System.out.println("Stop‑words: WYŁĄCZONE");
        }
    }
}