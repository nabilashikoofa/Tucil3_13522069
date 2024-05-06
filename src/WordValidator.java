import java.util.List;
import java.util.Scanner;

public class WordValidator {
    private final List<String> dictionary;
    private final Scanner scanner;

    public WordValidator(List<String> dictionary, Scanner scanner) {
        this.dictionary = dictionary;
        this.scanner = scanner;
    }

    public String getValidStartWord() {
        String startWord;
        do {
            System.out.print("Enter the start word: ");
            startWord = scanner.nextLine().toLowerCase();
            if (!isValidWord(startWord)) {
                System.out.println("Word '" + startWord + "' is not found in the dictionary. Please enter a valid word.");
            }
        } while (!isValidWord(startWord));
        return startWord;
    }

    public String getValidEndWord(String startWord) {
        String endWord;
        do {
            System.out.print("Enter the end word: ");
            endWord = scanner.nextLine().toLowerCase();
            if (!isValidWord(endWord)) {
                System.out.println("Word '" + endWord + "' is not found in the dictionary. Please enter a valid word.");
            } else if (endWord.length() != startWord.length()) {
                System.out.println("The end word must have the same length as the start word. Please enter a valid word.");
            } else if (endWord.equals(startWord)) {
                System.out.println("The end word cannot be the same as the start word. Please enter a different word.");
            }
        } while (!isValidWord(endWord) || endWord.length() != startWord.length());
        return endWord;
    }

    private boolean isValidWord(String word) {
        return dictionary.contains(word);
    }
}
