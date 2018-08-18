package com.lilithsthrone.utils;

import com.lilithsthrone.main.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This file was created by Adrian on 15.08.2018 for liliths-throne-public.
 */
public class FileUtils {

    /**
     * Returns the formatted date of the last modification of the given file.
     * @param file The file to check for the last modification
     * @return A string containing the date of the modification
     */
    public static String getFileTime(File file) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy - hh:mm");
        return dateFormat.format(file.lastModified());
    }

    /**
     * Compiles a list of all files in the given path matching the given pattern, e.g. "*.xml" for all XML files.
     * @param path The directory to search
     * @param pattern The file name pattern to include
     * @return A list of files matching the pattern
     */
    public static List<File> listFiles(String path, String pattern) {
        List<File> filesList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path), pattern)) {
            for (Path entry : stream) {
                filesList.add(entry.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        filesList.sort(Comparator.comparingLong(File::lastModified).reversed());
        return filesList;
    }

    /**
     * Copies a single file from the source to the target. Returns success and flashes a message if the operation fails.
     * @param source The path of the source file
     * @param target The path of the destination file
     * @return True if the operation succeeded, false otherwise
     */
    public static boolean copyFile(Path source, Path target) {
        try {
            Files.createDirectories(target);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Main.game.flashMessage(Colour.GENERIC_BAD, "Invalid file!");
            return false;
        }
    }

    /**
     * Copies a directory recursively from the source to the target. Returns success and flashes a message if the
     * operation fails.
     * @param sourcePath The path of the source directory
     * @param destinationPath The path of the destination directory
     * @return True if the operation succeeded, false otherwise
     */
    public static boolean copyDirectory(String sourcePath, String destinationPath) {
        Path sourceFolder = Paths.get(sourcePath);
        if (Files.exists(sourceFolder)) {
            Path destinationFolder = Paths.get(destinationPath);

            // Traverse the file tree and copy each file
            try (Stream<Path> fileStream = Files.walk(sourceFolder)) {
                fileStream.forEach(source -> {
                    Path destination = destinationFolder.resolve(sourceFolder.relativize(source));
                    copyFile(source, destination, false);
                });
            } catch (IOException e) {
                e.printStackTrace();
                Main.game.flashMessage(Colour.GENERIC_BAD, "Invalid folder!");
                return false;
            }
        }
        return true;
    }

    /**
     * Utility method to construct the path from the given string. See {@link FileUtils#deleteFile(Path)} for details.
     * @param path A string containing the file path
     */
    public static boolean deleteFile(String path) {
        return deleteFile(Paths.get(path));
    }

    /**
     * Deletes the file with the given path. Returns success and flashes a message if the operation fails.
     * @param p The path of the file
     * @return True if the operation succeeded, false otherwise
     */
    public static boolean deleteFile(Path p) {
        try {
            Files.deleteIfExists(p);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Main.game.flashMessage(Colour.GENERIC_BAD, "Invalid file!");
            return false;
        }
    }

    /**
     * Deletes the directory with the given path recursively. Returns success and flashes a message if the operation
     * fails.
     * @param path A string containing the folder path
     * @return True if the operation succeeded, false otherwise
     */
    public static boolean deleteDirectory(String path) {
        Path target = Paths.get(path);
        if (Files.exists(target)) {
            // Traverse the file tree in reverse order and delete each file
            try (Stream<Path> fileStream = Files.walk(target)) {
                fileStream.sorted(Comparator.reverseOrder())
//                        .peek(System.out::println)
                        .forEach(FileUtils::deleteFile);
            } catch (IOException e) {
                e.printStackTrace();
                Main.game.flashMessage(Colour.GENERIC_BAD, "Invalid folder!");
                return false;
            }
        }
        return true;
    }

}
