package budgetmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardGUI extends JFrame {
   
	private static final long serialVersionUID = 1L;
	private Budget_Manager budgetManager;
    private JLabel totalIncomeLabel;
    private JLabel totalExpensesLabel;
    private JLabel savingsLabel;

    public DashboardGUI(Budget_Manager budgetManager) {
        this.budgetManager = budgetManager;

        // Set up the frame
        setTitle("Personal Budget Manager - Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Financial summary labels
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalExpensesLabel = new JLabel("Total Expenses: $0.00");
        savingsLabel = new JLabel("Savings: $0.00");

        // Add labels to panel
        panel.add(totalIncomeLabel);
        panel.add(totalExpensesLabel);
        panel.add(savingsLabel);

        // Buttons for navigation
        JButton incomeButton = new JButton("Manage Income");
        JButton expenseButton = new JButton("Manage Expenses");

        // Add action listeners
        incomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DashboardGUI.this, "Income management not implemented yet!");
            }
        });

        expenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DashboardGUI.this, "Expense management not implemented yet!");
            }
        });

        // Add buttons to panel
        panel.add(incomeButton);
        panel.add(expenseButton);

        // Add panel to frame
        add(panel);

        // Refresh the summary
        refreshSummary();
    }

    // Method to refresh financial summary
    public void refreshSummary() {
        totalIncomeLabel.setText("Total Income: $" + budgetManager.getTotalIncome());
        totalExpensesLabel.setText("Total Expenses: $" + budgetManager.getTotalExpenses());
        savingsLabel.setText("Savings: $" + budgetManager.getSavings());
    }

    // Main method to test the dashboard
    public static void main(String[] args) {
        Budget_Manager manager = new Budget_Manager();
        manager.addIncome(new Income(5000.0, "Job", new java.util.Date()));
        manager.addExpense(new Expense(1200.0, "Rent", new java.util.Date()));

        SwingUtilities.invokeLater(() -> {
            DashboardGUI dashboard = new DashboardGUI(manager);
            dashboard.setVisible(true);
        });
    }
}

