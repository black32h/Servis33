package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String DIRECTORY_PATH = "downloaded_images"; // Шлях до директорії збереження зображень
    private static final int FILE_LIMIT = 10; // Ліміт кількості файлів у директорії
    private static final String ZIP_FILE_NAME = "images.zip"; // Ім'я ZIP-файлу для архіву

    public static void main(String[] args) {
        // Створення списку URL для завантаження зображень
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://img.freepik.com/free-vector/cute-koala-cartoon-character_1308-132636.jpg");
        imageUrls.add("https://png.pngtree.com/png-vector/20230930/ourmid/pngtree-panda-png-with-ai-generated-png-image_10153558.png");
        imageUrls.add("https://p7.hiclipart.com/preview/124/426/559/tiger-flame-lion-tigon-tiger.jpg");

        // Ініціалізація класу завантаження зображень
        ImageDownloader imageDownloader = new ImageDownloader(DIRECTORY_PATH);
        List<File> downloadedFiles = imageDownloader.downloadImages(imageUrls);

        // Перевірка: якщо завантажено більше 3 зображень, архівуємо їх
        if (downloadedFiles.size() > 3) {
            ZipManager.zipFiles(downloadedFiles, ZIP_FILE_NAME);
        }

        // Ініціалізація класу для перевірки директорії
        DirectoryManager directoryManager = new DirectoryManager(DIRECTORY_PATH);
        if (directoryManager.isFileLimitExceeded(FILE_LIMIT)) {
            File dir = new File(DIRECTORY_PATH);
            File[] files = dir.listFiles();
            if (files != null) {
                ZipManager.zipFiles(List.of(files), ZIP_FILE_NAME);
            }
        }
    }
}
