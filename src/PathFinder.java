import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PathFinder {
    public static List<String> constructPath(Map<String, String> parentMap, String end) {
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public static List<String> getNeighbors(String word, List<String> dictionary) {
        List<String> neighbors = new ArrayList<>();
        for (String dictWord : dictionary) {
            if (isNeighbor(word, dictWord)) {
                neighbors.add(dictWord);
            }
        }
        return neighbors;
    }

    private static boolean isNeighbor(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }
        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diffCount++;
            }
        }
        return diffCount == 1;
    }
}
