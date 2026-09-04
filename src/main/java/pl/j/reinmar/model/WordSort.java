package pl.j.reinmar.model;


// plik: model/WordSort.java

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Strategia sortowania dla listy WordCount.
 * Używa Collatora dla polskiej lokalizacji (pl-PL).
 *
 * Collator.PRIMARY ignoruje wielkość liter i akcenty.
 * Jeśli potrzebujesz pełnego rozróżnienia (case-sensitive + diakrytyki),
 * zmień na Collator.TERTIARY.
 */
public enum WordSort {

    /** Alfabetycznie wg polskiego Collatora. */
    ALPHABETIC {
        @Override
        public Comparator<WordCount> comparator() {
            return (a, b) -> localeStringComparator(Collator.PRIMARY)
                    .compare(a.word(), b.word());
        }
    },

    /** Najpierw liczba wystąpień malejąco, przy remisie alfabetycznie. */
    FREQUENCY_DESC {
        @Override
        public Comparator<WordCount> comparator() {
            Comparator<String> strCmp = localeStringComparator(Collator.PRIMARY);
            return (a, b) -> {
                int c = Integer.compare(b.count(), a.count());
                return (c != 0) ? c : strCmp.compare(a.word(), b.word());
            };
        }
    },

    /** Najpierw liczba wystąpień rosnąco, przy remisie alfabetycznie. */
    FREQUENCY_ASC {
        @Override
        public Comparator<WordCount> comparator() {
            Comparator<String> strCmp = localeStringComparator(Collator.PRIMARY);
            return (a, b) -> {
                int c = Integer.compare(a.count(), b.count());
                return (c != 0) ? c : strCmp.compare(a.word(), b.word());
            };
        }
    };

    /** Zwraca komparator dla danego trybu sortowania. */
    public abstract Comparator<WordCount> comparator();

    // ===== Pomocnicze: Collator dla PL =====
    private static Comparator<String> localeStringComparator(int strength) {
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        collator.setStrength(strength); // PRIMARY: ignoruje case/akcenty; TERTIARY: pełne rozróżnienie
        return collator::compare;
    }
}