package budgetmanager;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		Budget_Manager manager = new Budget_Manager();

        // incomes
        manager.addIncome(new Income(5000.0, "Job", new Date()));
        manager.addIncome(new Income(200.0, "Freelance", new Date()));

        //expenses
        manager.addExpense(new Expense(1200.0, "Rent", new Date()));
        manager.addExpense(new Expense(300.0, "Groceries", new Date()));

        // Display totals
        System.out.println("Total Income: " + manager.getTotalIncome());
        System.out.println("Total Expenses: " + manager.getTotalExpenses());
        System.out.println("Savings: " + manager.getSavings());

	}

}
