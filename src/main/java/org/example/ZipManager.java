package org.example;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipManager {

    // Метод для архівації файлів у ZIP
    public static void zipFiles(List<File> filesToZip, String zipFileName) {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (File file : filesToZip) {
                // Додавання файлу до архіву
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
                }
                zos.closeEntry();
                System.out.println("Файл додано до архіву: " + file.getName());
            }
            System.out.println("Зображення заархівовані у файл: " + zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
