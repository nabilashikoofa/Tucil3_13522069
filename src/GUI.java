import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private JTextField startWordField;
    private JTextField endWordField;
    private JTextArea resultArea;
    private JLabel executionTimeLabel;

    private static final String DICTIONARY_FILE = "dictionary.txt";
    private long startTime;
    private long endTime;

    private static final Color BLACK = Color.decode("#0F0B09");
    private static final Color GREY = Color.decode("#36312F");
    private static final Color DARK_PINK = Color.decode("#B96A80");
    private static final Color LIGHT_PINK = Color.decode("#F6C6D6");

    public GUI() {
        setTitle("Word Ladder Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BLACK);


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.setBackground(LIGHT_PINK);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Enter Words", 0, 0, new Font("Arial", Font.BOLD, 12), DARK_PINK));
        inputPanel.add(new JLabel("Start Word:"));
        startWordField = new JTextField();
        inputPanel.add(startWordField);
        inputPanel.add(new JLabel("End Word:"));
        endWordField = new JTextField();
        inputPanel.add(endWordField);

        JPanel algorithmPanel = new JPanel();
        algorithmPanel.setBackground(LIGHT_PINK);
        algorithmPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Select Algorithm", 0, 0, new Font("Arial", Font.BOLD, 12), DARK_PINK));
        String[] options = {"Uniform Cost Search (UCS)", "Greedy Best-First Search (GBFS)", "A* Search", "Compare All Algorithms"};
        JComboBox<String> algorithmComboBox = new JComboBox<>(options);
        algorithmPanel.add(algorithmComboBox);
        JButton solveButton = new JButton("Solve");
        solveButton.setBackground(DARK_PINK);
        solveButton.setForeground(LIGHT_PINK);
        solveButton.setFocusPainted(false);
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveWordLadder(algorithmComboBox.getSelectedIndex() + 1);
            }
        });
        algorithmPanel.add(solveButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, algorithmPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);
        splitPane.setBackground(DARK_PINK);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(LIGHT_PINK);
        resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Results", 0, 0, new Font("Arial", Font.BOLD, 12), DARK_PINK));
        executionTimeLabel = new JLabel("Execution time: ");
        executionTimeLabel.setForeground(DARK_PINK);
        resultPanel.add(executionTimeLabel, BorderLayout.NORTH);
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBackground(GREY);
        resultArea.setForeground(LIGHT_PINK);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_PINK);
        mainPanel.add(splitPane, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        JOptionPane.showMessageDialog(this, "Welcome to Word Ladder Solver!\nPlease enter the start and end words, then select an algorithm to find the word ladder.", "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }

    private void solveWordLadder(int choice) {
        startTime = System.currentTimeMillis();

        String startWord = startWordField.getText().trim();
        String endWord = endWordField.getText().trim();

        if (startWord.equals(endWord)) {
            JOptionPane.showMessageDialog(this, "Start and end words cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (startWord.length() != endWord.length()) {
            JOptionPane.showMessageDialog(this, "Start and end words must have the same length.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (startWord.isEmpty() || endWord.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both start and end words.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> dictionary = loadDictionary(DICTIONARY_FILE);

        if (choice == 4) {
            compareAllAlgorithms(startWord, endWord, dictionary);
        } else {
            WordLadderSearch wordLadderSearch = createAlgorithm(choice);
            SearchResult result = wordLadderSearch.findWordLadder(startWord, endWord, dictionary);
            displayResult(result);
        }

        endTime = System.currentTimeMillis();
        displayExecutionTime();
    }

    private void compareAllAlgorithms(String startWord, String endWord, List<String> dictionary) {
        StringBuilder comparisonResult = new StringBuilder("Comparing all algorithms...\n\n");

        startTime = System.currentTimeMillis();

        WordLadderSearch[] algorithms = {new UCS(), new GBFS(), new AStar()};

        for (WordLadderSearch algorithm : algorithms) {
            long algorithmStartTime = System.currentTimeMillis();
            SearchResult result = algorithm.findWordLadder(startWord, endWord, dictionary);
            long algorithmEndTime = System.currentTimeMillis();

            comparisonResult.append("==================================== ").append(algorithm.getClass().getSimpleName()).append(" ====================================\n");
            comparisonResult.append(getResultString(result));
            comparisonResult.append("\nExecution time: ").append(algorithmEndTime - algorithmStartTime).append(" ms\n\n");
        }

        endTime = System.currentTimeMillis();

        resultArea.setText(comparisonResult.toString());
    }

    private WordLadderSearch createAlgorithm(int choice) {
        switch (choice) {
            case 1:
                return new UCS();
            case 2:
                return new GBFS();
            case 3:
                return new AStar();
            default:
                throw new IllegalArgumentException("Invalid choice.");
        }
    }

    private List<String> loadDictionary(String filename) {
        List<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading dictionary file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return dictionary;
    }

    private void displayResult(SearchResult result) {
        resultArea.setText(getResultString(result));
    }

    private String getResultString(SearchResult result) {
        if (result.getPath().isEmpty()) {
            return "No ladder found between the words.";
        } else {
            return "Path found:\n" + String.join(" -> ", result.getPath()) +
                    "\nNumber of nodes visited: " + result.getNodeCount();
        }
    }

    private void displayExecutionTime() {
        long executionTime = endTime - startTime;
        executionTimeLabel.setText("Execution time: " + executionTime + " ms");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}
