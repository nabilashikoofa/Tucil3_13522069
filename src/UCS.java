import java.util.*;

public class UCS implements WordLadderSearch {
    @Override
    public SearchResult findWordLadder(String start, String end, List<String> dictionary) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> costMap = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));

        parentMap.put(start, null);
        costMap.put(start, 0);
        priorityQueue.add(new Node(start, 0)); 

        int nodeCount = 0;

        while (!priorityQueue.isEmpty()) {
            nodeCount++;
            Node currentNode = priorityQueue.poll();
            String currentWord = currentNode.getWord();

            if (currentWord.equals(end)) {
                return new SearchResult(PathFinder.constructPath(parentMap, end), nodeCount);
            }

            for (String neighbor : PathFinder.getNeighbors(currentWord, dictionary)) {
                int edgeCost = calculateEdgeCost(currentWord, neighbor);
                int newCost = costMap.get(currentWord) + edgeCost;

                if (costMap.computeIfAbsent(neighbor, k -> Integer.MAX_VALUE) > newCost) {
                    costMap.put(neighbor, newCost);
                    parentMap.put(neighbor, currentWord);
                    priorityQueue.add(new Node(neighbor, newCost)); 
                }
            }
        }

        return new SearchResult(Collections.emptyList(), nodeCount); // No ladder found
    }

    private static int calculateEdgeCost(String word1, String word2) {
        int cost = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                cost++;
            }
        }
        return cost;
    }
}
