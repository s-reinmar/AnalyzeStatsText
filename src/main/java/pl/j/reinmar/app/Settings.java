package pl.j.reinmar.app;

import pl.j.reinmar.model.WordSort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Settings {

    private final Set<String> stopWords = new HashSet<>(List.of(
            "i","oraz","że","to","w","na","z","do","się","jest","nie","a","o","po","u","ten","ta","to",
            "jak","który","która","które","te","dla","przy","albo","lub","czy","tam","tu","nad","pod",
            "od","bez","więc","co","tak","tylko","mnie","ciebie","jego","jej","ich"
    ));

    private int minWordLength = 2;

    private WordSort defaultSort = WordSort.FREQUENCY_DESC;

    public Set<String> getStopWords() {
        return stopWords;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public void setMinWordLength(int value) {
        this.minWordLength = Math.max(1, value);
    }

    public WordSort getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(WordSort sort) {
        this.defaultSort = sort;
    }

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