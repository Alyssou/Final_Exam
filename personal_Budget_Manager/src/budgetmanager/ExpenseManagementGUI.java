package budgetmanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseManagementGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Budget_Manager budgetManager;
    private DefaultTableModel tableModel;

    public ExpenseManagementGUI(Budget_Manager budgetManager) {
        this.budgetManager = budgetManager;

        // Set up the frame
        setTitle("Expense Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        JLabel dateLabel = new JLabel("Date (dd-MM-yyyy):");
        JTextField dateField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        JButton addButton = new JButton("Add Expense");

        formPanel.add(categoryLabel);
        formPanel.add(categoryField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Expense Records"));

        String[] columnNames = {"Category", "Amount (in $)", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Action listener for Add Expense button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = categoryField.getText();
                String amountText = amountField.getText();
                String dateText = dateField.getText();

                if (category.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(ExpenseManagementGUI.this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountText);
                    Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateText);

                    Expense expense = new Expense(amount, category, date);
                    budgetManager.addExpense(expense);

                    // Add to table
                    tableModel.addRow(new Object[]{category, amount, dateText});

                    // Clear fields
                    categoryField.setText("");
                    amountField.setText("");
                    dateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ExpenseManagementGUI.this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Populate table with existing expense records
        refreshExpenseTable();
    }

    private void refreshExpenseTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Expense expense : budgetManager.getExpenses()) {
            String date = new SimpleDateFormat("dd-MM-yyyy").format(expense.getDate());
            tableModel.addRow(new Object[]{expense.getCategory(), expense.getAmount(), date});
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        Budget_Manager manager = new Budget_Manager();
        manager.addExpense(new Expense(1200.0, "Rent", new Date()));

        SwingUtilities.invokeLater(() -> {
            ExpenseManagementGUI expenseGUI = new ExpenseManagementGUI(manager);
            expenseGUI.setVisible(true);
        });
    }
}
