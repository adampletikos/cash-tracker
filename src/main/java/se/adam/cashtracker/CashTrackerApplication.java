package se.adam.cashtracker;

import se.adam.cashtracker.cli.CashTrackerCLI;
import se.adam.cashtracker.repository.CsvTransactionRepository;
import se.adam.cashtracker.repository.TransactionRepository;
import se.adam.cashtracker.service.FinanceServices;

import java.nio.file.Path;

public class CashTrackerApplication {

    public static void main(String[] args) {

        Path path = Path.of("data/transactions.csv");

        TransactionRepository repository =
                new CsvTransactionRepository(path);

        FinanceServices service =
                new FinanceServices(repository);

        CashTrackerCLI cli =
                new CashTrackerCLI(service);

        cli.start();
    }
}