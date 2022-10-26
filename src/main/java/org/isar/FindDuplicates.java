package org.isar;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FindDuplicates {

    /**
     * Entry point of the application
     * @param args
     */
    public static void main(String[] args) {
        String folderPath = args[0];
        CompareMode compareMode = args.length > 1 ? CompareMode.from(args[1]) : CompareMode.SIZE_AND_NAME;
        System.out.println("Compare mode: " + compareMode);
        DuplicateFinder duplicateFinder = new DuplicateFinderImpl();
        Map<String, List<Path>> potentialDuplicateFiles = duplicateFinder.getCandidates(folderPath, compareMode);
        duplicateFinder.checkCandidates(potentialDuplicateFiles);
    }
}
