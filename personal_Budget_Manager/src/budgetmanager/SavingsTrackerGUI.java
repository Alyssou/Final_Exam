package budgetmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SavingsTrackerGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SavingsTrackerGUI(Budget_Manager budgetManager) {
        // Set up the frame
        setTitle("Savings Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Set Savings Goal"));

        JLabel goalLabel = new JLabel("Savings Goal:");
        JTextField goalField = new JTextField();
        JLabel currentSavingsLabel = new JLabel("Current Savings:");
        JTextField currentSavingsField = new JTextField(String.valueOf(budgetManager.getTotalIncome())); // assuming current savings is equal to total income initially

        JButton saveGoalButton = new JButton("Save Goal");

        formPanel.add(goalLabel);
        formPanel.add(goalField);
        formPanel.add(currentSavingsLabel);
        formPanel.add(currentSavingsField);
        formPanel.add(new JLabel());
        formPanel.add(saveGoalButton);

        // Progress panel
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout(10, 10));

        JLabel progressLabel = new JLabel("Savings Progress:");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(progressPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Action listener for Save Goal button
        saveGoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String goalText = goalField.getText();
                String currentSavingsText = currentSavingsField.getText();

                if (goalText.isEmpty() || currentSavingsText.isEmpty()) {
                    JOptionPane.showMessageDialog(SavingsTrackerGUI.this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double goalAmount = Double.parseDouble(goalText);
                    double currentSavings = Double.parseDouble(currentSavingsText);

                    budgetManager.setSavingsGoal(goalAmount, currentSavings);

                    // Update progress bar
                    double progress = currentSavings / goalAmount * 100;
                    progressBar.setValue((int) progress);
                    progressBar.setString(String.format("%.2f%%", progress));

                    // Clear fields
                    goalField.setText("");
                    currentSavingsField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SavingsTrackerGUI.this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

   
    public static void main(String[] args) {
        Budget_Manager manager = new Budget_Manager();
        manager.setSavingsGoal(10000.0, 2500.0);

        SwingUtilities.invokeLater(() -> {
            SavingsTrackerGUI savingsGUI = new SavingsTrackerGUI(manager);
            savingsGUI.setVisible(true);
        });
    }
}
