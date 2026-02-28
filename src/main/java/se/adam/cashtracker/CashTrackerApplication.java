package se.adam.cashtracker;

import se.adam.cashtracker.model.*;
import se.adam.cashtracker.repository.*;
import se.adam.cashtracker.service.FinanceServices;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public class CashTrackerApplication {

    public static void main(String[] args) {

        Path transactionPath = Path.of("data/transactions.csv");
        Path budgetPath = Path.of("data/budgets.csv");

        TransactionRepository transactionRepository =
                new CsvTransactionRepository(transactionPath);

        BudgetRepository budgetRepository =
                new CsvBudgetRepository(budgetPath);

        FinanceServices financeService =
                new FinanceServices(transactionRepository, budgetRepository);

        Transaction t1 = new Transaction(
                UUID.randomUUID(),
                LocalDate.of(2026, 3, 5),
                TransactionType.EXPENSE,
                Category.GROCERIES,
                new BigDecimal("1200"),
                "ICA",
                "Food shopping"
        );

        Transaction t2 = new Transaction(
                UUID.randomUUID(),
                LocalDate.of(2026, 3, 12),
                TransactionType.EXPENSE,
                Category.GROCERIES,
                new BigDecimal("800"),
                "Willys",
                "More food"
        );

        Transaction t3 = new Transaction(
                UUID.randomUUID(),
                LocalDate.of(2026, 3, 15),
                TransactionType.INCOME,
                Category.SALARY,
                new BigDecimal("25000"),
                "Company",
                "Salary"
        );

        transactionRepository.saveAll(List.of(t1, t2, t3));

        MonthlyBudget groceriesMarch = new MonthlyBudget(
                YearMonth.of(2026, 3),
                new BigDecimal("4000"),
                Category.GROCERIES
        );

        budgetRepository.save(groceriesMarch);

        BudgetComparison comparison =
                financeService.compareToBudget(
                        Category.GROCERIES,
                        YearMonth.of(2026, 3)
                );

        System.out.println("Budget: " + comparison.getBudget());
        System.out.println("Spent: " + comparison.getSpent());
        System.out.println("Remaining: " + comparison.getRemaining());
        System.out.println("Percentage used: "
                + comparison.getPercentageUsed() + "%");
        System.out.println("Over budget? "
                + comparison.isOverBudget());
    }
}