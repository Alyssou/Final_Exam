package budgetmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Budget_Manager {
    private List<Income> incomes;
    private List<Expense> expenses;
    private double savingsGoal;
    private double currentSavings;


    public Budget_Manager() {
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    // Add income
    public void addIncome(Income income) {
        incomes.add(income);
    }

    // Add expense
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
 
    // Get total income
    public double getTotalIncome() {
        double total = 0;
        for (Income income : incomes) {
            total += income.getAmount();
        }
        return total;
    }

    // Get total expenses
    public double getTotalExpenses() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    // Get savings 
    public double getSavings() {
        return getTotalIncome() - getTotalExpenses();
    }

    // Get list of incomes
    public List<Income> getIncomes() {
        return incomes;
    }

    // Get list of expenses
    public List<Expense> getExpenses() {
        return expenses;
    }
    
    public double getSavingsGoal() {
        return savingsGoal;
    }

    public void setSavingsGoal(double savingsGoal, double currentSavings) {
        this.savingsGoal = savingsGoal;
        this.currentSavings = currentSavings;
    }

    //  savings progress
    public double getSavingsProgress() {
        if (savingsGoal == 0) {
            return 0;
        }
        return (currentSavings / savingsGoal) * 100;
    }
    
    public Map<String, Double> getExpenseCategories() {
        Map<String, Double> categories = new HashMap<>();
        for (Expense expense : expenses) {
            categories.put(expense.getCategory(), categories.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        return categories;
    }
}

