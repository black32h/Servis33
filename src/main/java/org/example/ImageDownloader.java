package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageDownloader {
    // Директорія для збереження завантажених зображень
    private final String directoryPath;
    // Допустимі розширення файлів для зображень
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif");

    // Конструктор класу, що ініціалізує шлях до директорії
    public ImageDownloader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    // Метод для завантаження зображень за списком URL
    public List<File> downloadImages(List<String> imageUrls) {
        List<File> downloadedFiles = new ArrayList<>();
        DirectoryManager.createDirectoryIfNotExists(directoryPath);
        for (String imageUrl : imageUrls) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Перевірка відповіді сервера
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    System.err.println("Сервер повернув помилку: " + connection.getResponseCode());
                    continue;
                }

                // Перевірка, чи є контент зображенням
                if (!isImageContent(connection, imageUrl)) {
                    System.err.println("URL не вказує на зображення: " + imageUrl);
                    continue;
                }

                // Створення файлу для збереження завантаженого зображення
                File outputFile = new File(directoryPath, getImageName(imageUrl));

                // Перевірка, чи файл з таким ім'ям вже існує
                if (outputFile.exists()) {
                    outputFile = new File(directoryPath, System.currentTimeMillis() + "_" + getImageName(imageUrl));
                }

                // Збереження зображення у файл
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                downloadedFiles.add(outputFile);
                System.out.println("Зображення завантажено: " + imageUrl);

            } catch (MalformedURLException e) {
                System.err.println("Неправильний URL: " + imageUrl);
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Помилка завантаження зображення: " + imageUrl);
                e.printStackTrace();
            }
        }
        return downloadedFiles;
    }

    // Перевірка, чи є контент зображенням за типом або розширенням файлу
    private boolean isImageContent(HttpURLConnection connection, String imageUrl) {
        boolean isExtensionValid = ALLOWED_IMAGE_EXTENSIONS.stream().anyMatch(imageUrl.toLowerCase()::endsWith);
        if (!isExtensionValid) {
            String contentType = connection.getContentType();
            return contentType != null && contentType.startsWith("image/");
        }
        return true;
    }

    // Отримання імені файлу з URL
    private String getImageName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
    }
}
