import java.util.*;

public class GBFS implements WordLadderSearch {
    @Override
    public SearchResult findWordLadder(String start, String end, List<String> dictionary) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> heuristicMap = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getHeuristic));

        parentMap.put(start, null);
        heuristicMap.put(start, calculateHeuristic(start, end));
        priorityQueue.add(new Node(start, 0, heuristicMap.get(start), 0)); 

        int nodeCount = 0;

        while (!priorityQueue.isEmpty()) {
            nodeCount++;
            Node currentNode = priorityQueue.poll();
            String currentWord = currentNode.getWord();

            if (currentWord.equals(end)) {
                return new SearchResult(PathFinder.constructPath(parentMap, end), nodeCount);
            }

            for (String neighbor : PathFinder.getNeighbors(currentWord, dictionary)) {
                if (!parentMap.containsKey(neighbor)) {
                    parentMap.put(neighbor, currentWord);
                    heuristicMap.put(neighbor, calculateHeuristic(neighbor, end));
                    priorityQueue.add(new Node(neighbor, 0, heuristicMap.get(neighbor), 0)); 
                }
            }
        }

        return new SearchResult(Collections.emptyList(), nodeCount); // No ladder found
    }

    private int calculateHeuristic(String word, String target) {
        int heuristic = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                heuristic++;
            }
        }
        return heuristic;
    }
}
