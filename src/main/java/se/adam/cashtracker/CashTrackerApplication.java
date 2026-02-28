package se.adam.cashtracker;

import se.adam.cashtracker.cli.CashTrackerCLI;
import se.adam.cashtracker.repository.BudgetRepository;
import se.adam.cashtracker.repository.CsvBudgetRepository;
import se.adam.cashtracker.repository.CsvTransactionRepository;
import se.adam.cashtracker.repository.TransactionRepository;
import se.adam.cashtracker.service.FinanceServices;

import java.nio.file.Path;

public class CashTrackerApplication {

    public static void main(String[] args) {

        Path transactionPath = Path.of("data/transactions.csv");
        Path budgetPath = Path.of("data/budgets.csv");

        TransactionRepository transactionRepository =
                new CsvTransactionRepository(transactionPath);

        BudgetRepository budgetRepository =
                new CsvBudgetRepository(budgetPath);

        FinanceServices financeServices =
                new FinanceServices(transactionRepository, budgetRepository);

        CashTrackerCLI cli = new CashTrackerCLI(financeServices);

        cli.start();
    }
}