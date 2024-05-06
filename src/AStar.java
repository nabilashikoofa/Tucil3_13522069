import java.util.*;

public class AStar implements WordLadderSearch {
    @Override
    public SearchResult findWordLadder(String start, String end, List<String> dictionary) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gScoreMap = new HashMap<>();
        Map<String, Integer> fScoreMap = new HashMap<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getFScore));

        parentMap.put(start, null);
        gScoreMap.put(start, 0);
        fScoreMap.put(start, calculateFScore(start, end));

        openSet.add(new Node(start, 0, fScoreMap.get(start)));

        int nodeCount = 0;

        while (!openSet.isEmpty()) {
            nodeCount++;
            Node current = openSet.poll();
            String currentWord = current.getWord();

            if (currentWord.equals(end)) {
                return new SearchResult(PathFinder.constructPath(parentMap, end), nodeCount);
            }

            for (String neighbor : PathFinder.getNeighbors(currentWord, dictionary)) {
                int tentativeGScore = gScoreMap.get(currentWord) + 1;

                if (!gScoreMap.containsKey(neighbor) || tentativeGScore < gScoreMap.get(neighbor)) {
                    parentMap.put(neighbor, currentWord);
                    gScoreMap.put(neighbor, tentativeGScore);
                    fScoreMap.put(neighbor, tentativeGScore + calculateFScore(neighbor, end));
                    openSet.add(new Node(neighbor, tentativeGScore, fScoreMap.get(neighbor)));
                }
            }
        }

        return new SearchResult(Collections.emptyList(), nodeCount); // No ladder found
    }

    private static int calculateFScore(String word, String target) {
        int heuristic = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                heuristic++;
            }
        }
        return heuristic;
    }
}