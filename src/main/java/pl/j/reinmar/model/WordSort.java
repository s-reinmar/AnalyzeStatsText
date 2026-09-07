package pl.j.reinmar.model;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Określa kryteria sortowania zestawień słów i ich częstotliwości.
 * <p>
 * Typ wyliczeniowy udostępnia dedykowany {@link Comparator} dla obiektów {@link WordCount},
 * wykorzystując polski {@link Collator} w celu poprawnej obsługi polskich znaków diakrytycznych
 * podczas porównań alfabetycznych.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public enum WordSort {

    /**
     * Sortowanie alfabetyczne według polskiego {@link Collator} (od A do Z).
     */
    ALPHABETIC {
        @Override
        public Comparator<WordCount> comparator() {
            return (a, b) -> localeStringComparator(Collator.PRIMARY)
                    .compare(a.word(), b.word());
        }
    },

    /**
     * Sortowanie według liczby wystąpień malejąco. Przy jednakowej liczbie wystąpień
     * decyduje kolejność alfabetyczna.
     */
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

    /**
     * Sortowanie według liczby wystąpień rosnąco. Przy jednakowej liczbie wystąpień
     * decyduje kolejność alfabetyczna.
     */
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

    /**
     * Zwraca komparator obiektów {@link WordCount} odpowiadający wybranemu trybowi sortowania.
     *
     * @return instancja {@link Comparator} dopasowana do danego wariantu sortowania
     */
    public abstract Comparator<WordCount> comparator();

    /**
     * Tworzy i konfiguruje komparator ciągów znaków uwzględniający reguły języka polskiego.
     *
     * @param strength poziom dokładności porównywania w obiekcie {@link Collator} (np. {@link Collator#PRIMARY})
     * @return komparator ciągów znaków dopasowany do polskiej lokalizacji
     */
    private static Comparator<String> localeStringComparator(int strength) {
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        collator.setStrength(strength); // PRIMARY: ignoruje case/akcenty; TERTIARY: pełne rozróżnienie
        return collator::compare;
    }
}