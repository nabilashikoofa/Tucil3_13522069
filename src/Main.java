import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String[] options = {"GUI", "CLI"};
        int choice = JOptionPane.showOptionDialog(null, "Choose user interface", "Interface Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            SwingUtilities.invokeLater(new Runnable() { //  GUI
                @Override
                public void run() {
                    new GUI().setVisible(true);
                }
            });
        } else {
            WordLadderSolver.runWordLadderSolver();  // CLI
        }
    }
}
