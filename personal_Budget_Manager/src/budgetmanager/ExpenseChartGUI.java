package budgetmanager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseChartGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ExpenseChartGUI(Budget_Manager budgetManager) {
        // Set up the frame
        setTitle("Expense Category Chart");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Create pie chart for expenses
        JFreeChart chart = createExpenseChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(780, 550));
        
        // Add the chart to the main panel
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    // Method to create a pie chart displaying expense categories
    private JFreeChart createExpenseChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Get the expense data from BudgetManager (add this functionality in BudgetManager if necessary)
        Map<String, Double> expenseCategories = getExpenseCategories();

        // Populate the dataset with expense categories and amounts
        for (Map.Entry<String, Double> entry : expenseCategories.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Expenses by Category", // Title
                dataset               // Dataset                    // Tooltips
        );

        return chart;
    }

    // Method to get expense categories (you can modify this according to your data structure)
    private Map<String, Double> getExpenseCategories() {
        Map<String, Double> categories = new HashMap<>();
        
        // Example expenses data (you can replace this with real data)
        categories.put("Rent", 1200.00);
        categories.put("Food", 500.00);
        categories.put("Entertainment", 200.00);
        categories.put("Utilities", 150.00);
        
        // Here, you would collect expenses from the BudgetManager or database
        return categories;
    }

    // Main method for testing
    public static void main(String[] args) {
        Budget_Manager manager = new Budget_Manager();
        manager.addExpense(new Expense(1200.0, "Rent", new Date()));
        manager.addExpense(new Expense(500.00, "Food", new Date()));
        manager.addExpense(new Expense(200.00,"Entertainment", new Date()));
        manager.addExpense(new Expense(150.00, "Utilities",new Date()));

        SwingUtilities.invokeLater(() -> {
            ExpenseChartGUI chartGUI = new ExpenseChartGUI(manager);
            chartGUI.setVisible(true);
        });
    }
}
