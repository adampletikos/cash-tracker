package se.adam.cashtracker.repository;

import se.adam.cashtracker.model.MonthlyBudget;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.util.List;

public class CsvBudgetRepository implements BudgetRepository{
    private final Path filePath;

    public CsvBudgetRepository(Path filePath) {
        this.filePath = filePath;
    }

    public void save(MonthlyBudget budget) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("month,budget,category");
            writer.newLine();
            for (MonthlyBudget budget : budget) {
                writer.write(budget.toCsv());
                writer.newLine();
            }
        } catch (IOException e ) {
            throw new RuntimeException("Failed to save budget");
        }
    }

    public List<MonthlyBudget> findAll() {

    }

}
