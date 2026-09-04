package pl.j.reinmar.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileReader {

    /**
     * Odczytuje plik tekstowy z katalogu zasobów (Resources), zwraca jego zawartość jako ciąg znaków (String) z UTF-8
     * @param resourceName nazwa pliku w zasobach (np. "file.txt")
     * @return zawartość pliku jako String
     */
    public static String readResource(String resourceName) {
        InputStream in = FileReader.class
                .getClassLoader()
                .getResourceAsStream(resourceName);

        if (in == null) {
            throw new RuntimeException("Zasób został nieznaleziony: " + resourceName);
        }
        try (InputStream input = in) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania zasobów: " + resourceName, e);
        }
    }
}