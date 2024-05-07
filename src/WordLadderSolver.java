import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordLadderSolver {
    private final WordLadderSearch wordLadderSearch;
    private static final String DICTIONARY_FILE = "dictionary.txt";

    public WordLadderSolver(WordLadderSearch wordLadderSearch) {
        this.wordLadderSearch = wordLadderSearch;
    }

    public static void runWordLadderSolver() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;
            
            do {
                List<String> dictionary = loadDictionary(DICTIONARY_FILE);
                WordValidator wordValidator = new WordValidator(dictionary, scanner);
    
                String startWord = wordValidator.getValidStartWord();
                String endWord = wordValidator.getValidEndWord(startWord);
    
                int choice = selectAlgorithm(scanner);
    
                if (choice == 4) {
                    compareAllAlgorithms(startWord, endWord);
                } else {
                    WordLadderSearch wordLadderSearch = createAlgorithm(choice);
                    WordLadderSolver solver = new WordLadderSolver(wordLadderSearch);
                    solver.solve(startWord, endWord);
                }
                
                System.out.print("Do you want to find another word ladder? (yes/no): ");
                String input = scanner.nextLine().toLowerCase();
                exit = !input.equals("yes");
            } while (!exit);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int selectAlgorithm(Scanner scanner) {
        System.out.println("Choose an algorithm to use:");
        System.out.println("1. Uniform Cost Search (UCS)");
        System.out.println("2. Greedy Best-First Search (GBFS)");
        System.out.println("3. A* Search");
        System.out.println("4. Compare All Algorithms");

        int choice = getValidChoice(scanner, 1, 4);
        return choice;
    }

    private static int getValidChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
            }
        }
        return choice;
    }


    private static WordLadderSearch createAlgorithm(int choice) {
        switch (choice) {
            case 1:
                return new UCS();
            case 2:
                return new GBFS();
            case 3:
                return new AStar();
            case 4:
                return null;
            default:
                throw new IllegalArgumentException("Invalid choice.");
        }
    }

    private static void compareAllAlgorithms(String startWord, String endWord) {
        System.out.println("Comparing all algorithms...");

        WordLadderSearch[] algorithms = {new UCS(), new GBFS(), new AStar()};

        for (WordLadderSearch algorithm : algorithms) {
            WordLadderSolver solver = new WordLadderSolver(algorithm);
            System.out.println("===================================== " + algorithm.getClass().getSimpleName() + " =====================================");
            solver.solve(startWord, endWord);
        }
    }

    private static List<String> loadDictionary(String filename) {
        List<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file.");
            e.printStackTrace();
        }
        return dictionary;
    }

    public void solve(String startWord, String endWord) {
        List<String> dictionary = loadDictionary(DICTIONARY_FILE);

        long startTime = System.currentTimeMillis();
        SearchResult result = wordLadderSearch.findWordLadder(startWord, endWord, dictionary);
        long endTime = System.currentTimeMillis();

        printResult(result, endTime - startTime);
    }

    private void printResult(SearchResult result, long executionTime) {
        if (result.getPath().isEmpty()) {
            System.out.println("No ladder found between the words.");
        } else {
            System.out.println("Path found:");
            System.out.println(String.join(" -> ", result.getPath()));
        }
        System.out.println("Number of nodes visited: " + result.getNodeCount());
        System.out.println("Execution time: " + executionTime + " ms");
    }
}
