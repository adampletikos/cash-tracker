package se.adam.cashtracker;

import se.adam.cashtracker.model.Category;
import se.adam.cashtracker.model.MonthlyBudget;
import se.adam.cashtracker.repository.BudgetRepository;
import se.adam.cashtracker.repository.CsvBudgetRepository;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.YearMonth;

public class CashTrackerApplication {

    public static void main(String[] args) {

        // Path to budgets.csv (adjust if needed)
        Path budgetPath = Path.of("data/budgets.csv");

        BudgetRepository budgetRepository = new CsvBudgetRepository(budgetPath);

        // 1️⃣ Create a budget
        MonthlyBudget groceriesMarch = new MonthlyBudget(
                YearMonth.of(2026, 3),
                new BigDecimal("4000"),
                Category.GROCERIES
        );

        // 2️⃣ Save it
        budgetRepository.save(groceriesMarch);

        System.out.println("Saved budget for March 2026 (Groceries).");

        // 3️⃣ Print all budgets
        System.out.println("\nAll budgets:");
        for (MonthlyBudget b : budgetRepository.findAll()) {
            System.out.println(b.formatted());
        }

        // 4️⃣ Try finding a specific one
        System.out.println("\nSearching for March 2026 Groceries budget...");

        budgetRepository.findByCategoryAndMonth(
                Category.GROCERIES,
                YearMonth.of(2026, 3)
        ).ifPresentOrElse(
                b -> System.out.println("Found: " + b.getAmount()),
                () -> System.out.println("No budget found.")
        );
    }
}