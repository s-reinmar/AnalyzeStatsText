package pl.j.reinmar.io;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Klasa narzędziowa odpowiedzialna za odczyt plików z zasobów aplikacji (classpath).
 * <p>
 * Dostarcza statyczne metody pomocnicze pozwalające na łatwe wczytywanie zawartości
 * plików tekstowych do ciągów znaków ({@link String}) z wykorzystaniem kodowania UTF-8.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class FileReader {

    /**
     * Prywatny konstruktor zapobiegający tworzeniu instancji klasy narzędziowej.
     */
    private FileReader() {
    }

    /**
     * Wczytuje plik z zasobów aplikacji (classpath) i zwraca jego zawartość jako ciąg znaków w kodowaniu UTF-8.
     *
     * @param path ścieżka do pliku zasobu względem katalogu zasobów (np. {@code "texts/file.txt"})
     * @return zawartość pliku w postaci ciągu znaków {@link String}
     * @throws RuntimeException jeśli zasób nie zostanie odnaleziony w classpath lub wystąpi błąd odczytu I/O
     * @throws IOException      jeśli wystąpi błąd podczas zamykania strumienia wejściowego
     */
    public static String readResource(String path) throws IOException {

        InputStream in = FileReader.class
                .getClassLoader()
                .getResourceAsStream(path);

        if (in == null) {
            throw new RuntimeException("Zasób został nieznaleziony: " + path);
        }
        try (InputStream input = in) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania zasobów: " + path, e);
        }
    }
}