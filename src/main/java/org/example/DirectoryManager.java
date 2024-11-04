package org.example;

import java.io.File;

public class DirectoryManager {
    private final String directoryPath;

    // Конструктор класу, що ініціалізує шлях до директорії
    public DirectoryManager(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    // Метод для створення директорії, якщо вона не існує
    public static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Директорію створено: " + directoryPath);
        }
    }

    // Метод для перевірки кількості файлів у директорії та перевищення ліміту
    public boolean isFileLimitExceeded(int fileLimit) {
        File dir = new File(directoryPath);
        return dir.exists() && dir.listFiles().length >= fileLimit;
    }
}
