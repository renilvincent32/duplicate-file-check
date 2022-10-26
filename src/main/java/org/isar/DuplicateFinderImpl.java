package org.isar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DuplicateFinderImpl implements DuplicateFinder {

    /**
     * Get potential duplicate files by size, name or both, depending on the CompareMode
     * @param folderPath
     * @param mode
     * @return duplicate files grouped by size, name or both
     */
    public Map<String, List<Path>> getCandidates(String folderPath, CompareMode mode) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            List<Path> allFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> FilenameUtils.getExtension(path.toString()).equals("txt"))
                    .toList();
            return getIdenticalFilesByCompareMode(allFiles, mode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compare the potential duplicates within a group, based on MD5 hash, and identify duplicate amongst groups
     * @param duplicateFiles
     * @return List of duplicate files
     */
    public void checkCandidates(Map<String, List<Path>> duplicateFiles) {

        /**
         * Create a map with MD5 hash as key and the list of files having the same hash, as the value.
         */

        Map<String, List<Path>> fileMd5HashMap = duplicateFiles.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toMap(DuplicateFinderImpl::getMd5Hash, path -> {
                    List<Path> files = new ArrayList<>();
                    files.add(path);
                    return files;
                }, (existing, newList) -> {
                    newList.addAll(existing);
                    return newList;
                }));

        /**
         * Use the above map to filter out files having the same MD5 hash, and print them to console.
         */

        fileMd5HashMap.values()
                        .stream()
                        .filter(paths -> paths.size() > 1)
                        .forEach(files -> {
                            System.out.println("Duplicate files: " + files);
                        });
    }

    /**
     * Switch between various compare modes and group potential duplicate files accordingly.
     * @param allFiles
     * @param compareMode
     * @return
     * @throws IOException
     */
    private Map<String, List<Path>> getIdenticalFilesByCompareMode(List<Path> allFiles, CompareMode compareMode) throws IOException {
        Map<String, List<Path>> duplicateFiles = new HashMap<>();
        switch (compareMode) {
            case SIZE -> groupDuplicateFilesBySize(allFiles, duplicateFiles);
            case NAME -> groupDuplicateFilesByName(allFiles, duplicateFiles);
            case SIZE_AND_NAME -> {
                groupDuplicateFilesBySize(allFiles, duplicateFiles);
                List<Path> duplicateFilesBySize = duplicateFiles.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .toList();
                duplicateFiles = new HashMap<>();
                groupDuplicateFilesByName(duplicateFilesBySize, duplicateFiles);
            }
        }
        return duplicateFiles;
    }

    private void groupDuplicateFilesByName(List<Path> files, Map<String, List<Path>> duplicateFiles) {
        List<String> distinctFileNames = getFileNames(files);
        Map<String, Long> noOfEntriesForEachFileName = distinctFileNames
                .stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        for (Path file : files) {
            String fileName = file.getFileName().toString().substring(0, 5);
            if (noOfEntriesForEachFileName.get(fileName) > 1) {
                duplicateFiles.computeIfPresent(fileName, (fn, dupFiles) -> {
                    dupFiles.add(file);
                    return dupFiles;
                });
                duplicateFiles.computeIfAbsent(fileName, fn -> {
                    List<Path> dupFiles = new ArrayList<>();
                    dupFiles.add(file);
                    return dupFiles;
                });
            }
        }
    }

    private void groupDuplicateFilesBySize(List<Path> files, Map<String, List<Path>> duplicateFiles) throws IOException {
        List<Long> fileSizes = getFileSizes(files);
        Map<Long, Long> noOfEntriesForEachFileSize = fileSizes
                .stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        for (Path file : files) {
            Long fileSize = Files.size(file);
            if (noOfEntriesForEachFileSize.get(fileSize) > 1) {
                duplicateFiles.computeIfPresent(fileSize.toString(), (fn, dupFiles) -> {
                    dupFiles.add(file);
                    return dupFiles;
                });
                duplicateFiles.computeIfAbsent(fileSize.toString(), fn -> {
                    List<Path> dupFiles = new ArrayList<>();
                    dupFiles.add(file);
                    return dupFiles;
                });
            }
        }
    }

    private List<Long> getFileSizes(List<Path> files) {
        return files
                .stream()
                .map(file -> {
                    try {
                        return Files.size(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    private List<String> getFileNames(List<Path> files) {
        return files
                .stream()
                .map(Path::getFileName)
                .map(Object::toString)
                .map(str -> str.substring(0, 5))
                .toList();
    }

    private static String getMd5Hash(Path file) {
        String md5 = null;
        try (InputStream is = Files.newInputStream(file)) {
            md5 = DigestUtils.md5Hex(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return md5;
    }
}
