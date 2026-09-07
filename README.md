**TextAnalyzer**

Wielomodułowa aplikacja w języku Java przeznaczona do analizy tekstowej, zliczania częstotliwości słów, generowania statystyk oraz eksportu raportów do formatów TXT, CSV, JSON i XML. Aplikacja udostępnia interfejs konsolowy (CLI) oraz graficzny (JavaFX).

**Funkcjonalności**

Zliczanie słów, znaków (ze spacjami i bez) oraz zdań.
Analiza częstotliwości słów z uwzględnieniem minimalnej długości słowa oraz listy stop-words.
Wyszukiwanie najczęstszych wyrazów (Top N).
Sortowanie alfabetyczne (z obsługą polskich znaków przez Collator) oraz według częstotliwości.
Eksport raportów do plików TXT, CSV, JSON oraz XML.
Interfejs graficzny JavaFX z obsługą okien dialogowych, wyboru plików oraz podglądem konsoli.

**Architektura pakiety**

pl.j.reinmar.app — Klasy startowe (Main, Settings, obsługa menu)
pl.j.reinmar.core — Silnik analityczny, tokenizacja, zapis raportów
pl.j.reinmar.fx — Moduł GUI JavaFX (App, FxController, FxUserInput)
pl.j.reinmar.model — Rekordy i typy danych (TextStats, WordCount, WordSort)
pl.j.reinmar.ui — Formatery (TXT, CSV, JSON, XML), drukarki statystyk, CLI
pl.j.reinmar.util — Klasy narzędziowe (Escape, FileReader, TimeUtil)

**Wymagania**

Java Development Kit (JDK) 17 lub nowszy
Maven
