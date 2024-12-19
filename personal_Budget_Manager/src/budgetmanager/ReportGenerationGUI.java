package budgetmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ReportGenerationGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Budget_Manager budgetManager;

    public ReportGenerationGUI(Budget_Manager budgetManager) {
        this.budgetManager = budgetManager;

        // Set up the frame
        setTitle("Generate Financial Report");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Select Report Period"));

        JLabel periodLabel = new JLabel("Report Period:");
        JComboBox<String> periodComboBox = new JComboBox<>(new String[]{"Monthly", "Yearly"});

        JButton generateReportButton = new JButton("Generate Report");

        formPanel.add(periodLabel);
        formPanel.add(periodComboBox);
        formPanel.add(new JLabel());  // Empty label for spacing
        formPanel.add(generateReportButton);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Action listener for Generate Report button
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String period = (String) periodComboBox.getSelectedItem();

               
                String report = generateReport(period);

                // Display the report in a new window
                JTextArea textArea = new JTextArea(report);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);

                JFrame reportFrame = new JFrame("Financial Report");
                reportFrame.setSize(600, 400);
                reportFrame.setLocationRelativeTo(null);
                reportFrame.add(scrollPane);
                reportFrame.setVisible(true);

                // Optionally, save the report to a file
                saveReportToFile(report);
            }
        });
    }

    
    private String generateReport(String period) {
        StringBuilder report = new StringBuilder();

        report.append("Financial Report\n");
        report.append("Period: ").append(period).append("\n\n");

        double totalIncome = budgetManager.getTotalIncome();
        double totalExpenses = budgetManager.getTotalExpenses();
        double currentSavings = budgetManager.getSavings();
        double savingsGoal = budgetManager.getSavingsGoal();

        report.append("Total Income: ").append(totalIncome).append("\n");
        report.append("Total Expenses: ").append(totalExpenses).append("\n");
        report.append("Current Savings: ").append(currentSavings).append("\n");
        report.append("Savings Goal: ").append(savingsGoal).append("\n");
        report.append("Savings Progress: ").append(budgetManager.getSavingsProgress()).append("%\n");

        return report.toString();
    }

    // Method to save the report to a text file
    private void saveReportToFile(String report) {
        try (FileWriter writer = new FileWriter("financial_report.txt")) {
            writer.write(report);
            JOptionPane.showMessageDialog(this, "Report saved to financial_report.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public static void main(String[] args) {
        Budget_Manager manager = new Budget_Manager();
        manager.setSavingsGoal(10000.0, 2500.0);
        manager.addIncome(new Income(200.0, "Freelance", new Date()));
        manager.addExpense(new Expense(1200.0, "Rent", new Date()));

        SwingUtilities.invokeLater(() -> {
            ReportGenerationGUI reportGUI = new ReportGenerationGUI(manager);
            reportGUI.setVisible(true);
        });
    }
}
