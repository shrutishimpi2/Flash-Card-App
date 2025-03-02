import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class FlashcardApp {
    private static ArrayList<String[]> flashcards = new ArrayList<>();
    private static int currentIndex = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flashcard App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        
        frame.setVisible(true);
    }
    
    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);
        
        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setBounds(10, 20, 80, 25);
        panel.add(questionLabel);
        
        JTextField questionText = new JTextField(20);
        questionText.setBounds(100, 20, 200, 25);
        panel.add(questionText);
        
        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setBounds(10, 60, 80, 25);
        panel.add(answerLabel);
        
        JTextField answerText = new JTextField(20);
        answerText.setBounds(100, 60, 200, 25);
        panel.add(answerText);
        
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(10, 100, 80, 25);
        panel.add(saveButton);
        
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(100, 100, 80, 25);
        panel.add(nextButton);
        
        JButton loadButton = new JButton("Review");
        loadButton.setBounds(190, 100, 100, 25);
        panel.add(loadButton);
        
        JLabel reviewLabel = new JLabel("Flashcard: ");
        reviewLabel.setBounds(10, 140, 300, 25);
        panel.add(reviewLabel);
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = questionText.getText().trim();
                String answer = answerText.getText().trim();
                if (!question.isEmpty() && !answer.isEmpty()) {
                    flashcards.add(new String[]{question, answer});
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("flashcards.txt", true))) {
                        writer.write(question + "|" + answer + "\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    questionText.setText("");
                    answerText.setText("");
                    showStyledMessage("Success", "Flashcard saved successfully!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showStyledMessage("Warning", "Both question and answer are required!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flashcards.clear();
                try (BufferedReader reader = new BufferedReader(new FileReader("flashcards.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        flashcards.add(line.split("\\|"));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (!flashcards.isEmpty()) {
                    currentIndex = 0;
                    reviewLabel.setText("Flashcard: " + flashcards.get(currentIndex)[0]);
                } else {
                    showStyledMessage("Info", "No flashcards available. Please add some first.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flashcards.isEmpty()) {
                    currentIndex = (currentIndex + 1) % flashcards.size();
                    reviewLabel.setText("Flashcard: " + flashcards.get(currentIndex)[0] + " - " + flashcards.get(currentIndex)[1]);
                } else {
                    showStyledMessage("Info", "No flashcards available to review.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    
    private static void showStyledMessage(String title, String message, int messageType) {
        UIManager.put("OptionPane.messageForeground", Color.BLUE);
        UIManager.put("Panel.background", new Color(255, 228, 196)); // Light color background
        UIManager.put("OptionPane.background", new Color(255, 228, 196));
        UIManager.put("Button.background", new Color(144, 238, 144)); // Light green buttons
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }
}
