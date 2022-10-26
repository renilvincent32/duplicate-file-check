package org.isar;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface DuplicateFinder {

    Map<String, List<Path>> getCandidates(String folderPath, CompareMode mode);

    void checkCandidates(Map<String, List<Path>> candidates);

}
