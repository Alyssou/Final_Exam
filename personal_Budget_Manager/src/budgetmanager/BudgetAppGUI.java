package budgetmanager;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BudgetAppGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private double totalIncome = 0;
    private double totalExpenses = 0;
    private double savingsGoal = 0;
    private DefaultTableModel incomeTableModel;
    private DefaultTableModel expenseTableModel;
    private JLabel totalIncomeValueLabel, totalExpensesValueLabel, netBalanceValueLabel, savingsStatusLabel, savingsGoalLabel;
    private DefaultPieDataset pieDataset;
    private JProgressBar savingsProgressBar;

    // Custom colors
    private final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private final Color INCOME_COLOR = new Color(34, 139, 34);
    private final Color EXPENSE_COLOR = new Color(255, 69, 0);
    private final Color ACCENT_COLOR = new Color(255, 140, 0);

    public BudgetAppGUI() {
        setTitle("Personal Budget Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Set custom look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add Header
        add(createHeaderBar(), BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(SECONDARY_COLOR);

        // Add Cards
        mainPanel.add(createIncomeCard());
        mainPanel.add(createExpenseCard());
        mainPanel.add(createSummaryCard());
        mainPanel.add(createChartCard());

        add(mainPanel, BorderLayout.CENTER);

        // Footer with Buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footerPanel.setBackground(new Color(230, 240, 250));

        JButton setGoalButton = createStyledButton("Set Savings Goal", ACCENT_COLOR);
        setGoalButton.addActionListener(e -> openSavingsGoalDialog());
        footerPanel.add(setGoalButton);

        JButton reportButton = createStyledButton("Generate Report", PRIMARY_COLOR);
        reportButton.addActionListener(e -> generateReport());
        footerPanel.add(reportButton);

        JButton closeButton = createStyledButton("Close", EXPENSE_COLOR);
        closeButton.addActionListener(e -> dispose());
        footerPanel.add(closeButton);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderBar() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Personal Budget Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel dateLabel = new JLabel(new SimpleDateFormat("MMMM dd, yyyy").format(new Date()));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createIncomeCard() {
        JPanel card = createCardPanel("Income");

        // Table for Income
        incomeTableModel = new DefaultTableModel(new Object[]{"Date", "Amount", "Source"}, 0);
        JTable incomeTable = new JTable(incomeTableModel);
        styleTable(incomeTable);
        JScrollPane scrollPane = new JScrollPane(incomeTable);
        card.add(scrollPane, BorderLayout.CENTER);

        JButton addIncomeButton = createStyledButton("Add Income", INCOME_COLOR);
        addIncomeButton.addActionListener(e -> openIncomeDialog());
        card.add(addIncomeButton, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createExpenseCard() {
        JPanel card = createCardPanel("Expenses");

        // Table for Expenses
        expenseTableModel = new DefaultTableModel(new Object[]{"Date", "Amount", "Category"}, 0);
        JTable expenseTable = new JTable(expenseTableModel);
        styleTable(expenseTable);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        card.add(scrollPane, BorderLayout.CENTER);

        JButton addExpenseButton = createStyledButton("Add Expense", EXPENSE_COLOR);
        addExpenseButton.addActionListener(e -> openExpenseDialog());
        card.add(addExpenseButton, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createSummaryCard() {
        JPanel card = createCardPanel("Summary");

        JPanel summaryPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        summaryPanel.setBackground(Color.WHITE);
        card.add(summaryPanel, BorderLayout.CENTER);

        totalIncomeValueLabel = createSummaryLabel("Total Income: $0");
        summaryPanel.add(totalIncomeValueLabel);

        totalExpensesValueLabel = createSummaryLabel("Total Expenses: $0");
        summaryPanel.add(totalExpensesValueLabel);

        netBalanceValueLabel = createSummaryLabel("Net Balance: $0");
        summaryPanel.add(netBalanceValueLabel);

        savingsGoalLabel = createSummaryLabel("Savings Goal: Not Set");
        summaryPanel.add(savingsGoalLabel);

        savingsStatusLabel = createSummaryLabel("Savings Status: No Goal Set");
        summaryPanel.add(savingsStatusLabel);

        savingsProgressBar = new JProgressBar(0, 100);
        savingsProgressBar.setStringPainted(true);
        savingsProgressBar.setForeground(INCOME_COLOR);
        summaryPanel.add(savingsProgressBar);

        return card;
    }

    private JPanel createChartCard() {
        JPanel card = createCardPanel("Overview Chart");

        pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Income", totalIncome);
        pieDataset.setValue("Expenses", totalExpenses);

        JFreeChart pieChart = ChartFactory.createPieChart("Income vs Expenses", pieDataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint("Income", INCOME_COLOR);
        plot.setSectionPaint("Expenses", EXPENSE_COLOR);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        pieChart.setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        card.add(chartPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new LineBorder(new Color(200, 200, 200), 1, true)));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        card.add(titleLabel, BorderLayout.NORTH);

        return card;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
    }

    private void openIncomeDialog() {
        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        JTextField amountField = new JTextField(10);
        JTextField sourceField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Source:"));
        panel.add(sourceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Income",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String date = dateField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String source = sourceField.getText();
                incomeTableModel.addRow(new Object[]{date, amount, source});
                totalIncome += amount;
                updateSummaryAndChart();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered.");
            }
        }
    }

    private void openExpenseDialog() {
        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        JTextField amountField = new JTextField(10);
        JTextField categoryField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Expense",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String date = dateField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                expenseTableModel.addRow(new Object[]{date, amount, category});
                totalExpenses += amount;
                updateSummaryAndChart();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered.");
            }
        }
    }

    private void openSavingsGoalDialog() {
        String goalStr = JOptionPane.showInputDialog(this, "Set Your Savings Goal:");
        try {
            savingsGoal = Double.parseDouble(goalStr);
            savingsGoalLabel.setText("Savings Goal: $" + String.format("%.2f", savingsGoal));
            updateSummaryAndChart();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid goal entered.");
        }
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Personal Budget Report\n\n");
        report.append("Total Income: $").append(String.format("%.2f", totalIncome)).append("\n");
        report.append("Total Expenses: $").append(String.format("%.2f", totalExpenses)).append("\n");
        report.append("Net Balance: $").append(String.format("%.2f", totalIncome - totalExpenses)).append("\n");
        report.append("Savings Goal: $").append(String.format("%.2f", savingsGoal)).append("\n\n");

        report.append("Income Breakdown:\n");
        for (int i = 0; i < incomeTableModel.getRowCount(); i++) {
            report.append(incomeTableModel.getValueAt(i, 0)).append(" - $")
                  .append(incomeTableModel.getValueAt(i, 1)).append(" - ")
                  .append(incomeTableModel.getValueAt(i, 2)).append("\n");
        }

        report.append("\nExpense Breakdown:\n");
        for (int i = 0; i < expenseTableModel.getRowCount(); i++) {
            report.append(expenseTableModel.getValueAt(i, 0)).append(" - $")
                  .append(expenseTableModel.getValueAt(i, 1)).append(" - ")
                  .append(expenseTableModel.getValueAt(i, 2)).append("\n");
        }

        JTextArea textArea = new JTextArea(25, 50);
        textArea.setEditable(false);
        textArea.setText(report.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Budget Report", JOptionPane.PLAIN_MESSAGE);
    }

    private void updateSummaryAndChart() {
        double netBalance = totalIncome - totalExpenses;
        totalIncomeValueLabel.setText("Total Income: $" + String.format("%.2f", totalIncome));
        totalExpensesValueLabel.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
        netBalanceValueLabel.setText("Net Balance: $" + String.format("%.2f", netBalance));

        if (savingsGoal > 0) {
            double savingsProgress = (netBalance / savingsGoal) * 100;
            savingsProgressBar.setValue((int) savingsProgress);
            savingsProgressBar.setString(String.format("%.1f%%", savingsProgress));

            if (netBalance >= savingsGoal) {
                savingsStatusLabel.setText("Savings Status: Goal Achieved!");
                savingsStatusLabel.setForeground(INCOME_COLOR);
            } else {
                double savingsLeft = savingsGoal - netBalance;
                savingsStatusLabel.setText("Savings Status: $" + String.format("%.2f", savingsLeft) + " to Goal");
                savingsStatusLabel.setForeground(EXPENSE_COLOR);
            }
        }

        pieDataset.setValue("Income", totalIncome);
        pieDataset.setValue("Expenses", totalExpenses);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BudgetAppGUI().setVisible(true);
        });
    }
}