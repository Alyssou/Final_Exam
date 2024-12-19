package budgetmanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomeManagementGUI extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private Budget_Manager budgetManager;
    private DefaultTableModel tableModel;

    public IncomeManagementGUI(Budget_Manager budgetManager) {
        this.budgetManager = budgetManager;

        // Set up the frame
        setTitle("Income Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Income"));

        JLabel sourceLabel = new JLabel("Source:");
        JTextField sourceField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        JLabel dateLabel = new JLabel("Date (dd-MM-yyyy):");
        JTextField dateField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        JButton addButton = new JButton("Add Income");

        formPanel.add(sourceLabel);
        formPanel.add(sourceField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Income Records"));

        String[] columnNames = {"Source", "Amount (in $) ", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable incomeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(incomeTable);

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Action listener for Add Income button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String source = sourceField.getText();
                String amountText = amountField.getText();
                String dateText = dateField.getText();

                if (source.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(IncomeManagementGUI.this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountText);
                    Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateText);

                    Income income = new Income(amount, source, date);
                    budgetManager.addIncome(income);

                    // Add to table
                    tableModel.addRow(new Object[]{source, amount, dateText});

                    // Clear fields
                    sourceField.setText("");
                    amountField.setText("");
                    dateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(IncomeManagementGUI.this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Populate table with existing income records
        refreshIncomeTable();
    }

    private void refreshIncomeTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Income income : budgetManager.getIncomes()) {
            String date = new SimpleDateFormat("dd-MM-yyyy").format(income.getDate());
            tableModel.addRow(new Object[]{income.getSource(), income.getAmount(), date});
        }
    }

    
    public static void main(String[] args) {
        Budget_Manager manager = new Budget_Manager();
        manager.addIncome(new Income(5000.0, "Job", new Date()));

        SwingUtilities.invokeLater(() -> {
            IncomeManagementGUI incomeGUI = new IncomeManagementGUI(manager);
            incomeGUI.setVisible(true);
        });
    }
}
