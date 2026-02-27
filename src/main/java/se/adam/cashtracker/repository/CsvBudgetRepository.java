package se.adam.cashtracker.repository;

import se.adam.cashtracker.model.Category;
import se.adam.cashtracker.model.MonthlyBudget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvBudgetRepository implements BudgetRepository{
    private final Path filePath;

    public CsvBudgetRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
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

    @Override
    public List<MonthlyBudget> findAll() {
        List<MonthlyBudget> budgets = new ArrayList<>();

        if (!Files.exists(filePath)) {return budgets;}

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] budgetCSV = line.split(",");
                if (budgetCSV.length != 3) {
                    throw new IllegalStateException("Invalid budget row: " + line);
                }

                MonthlyBudget budget = new MonthlyBudget(
                        YearMonth.parse(budgetCSV[0]),
                        new BigDecimal(budgetCSV[1]),
                        Category.valueOf(budgetCSV[2]));
                budgets.add(budget);
            }

            return budgets;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read budgets",e);
        }
    }

    @Override
    public Optional<MonthlyBudget> findByCategoryAndMonth(Category category, YearMonth month) {
        List<MonthlyBudget> budgets = findAll();
        for (MonthlyBudget budget : budgets) {
            if (budget.getCategory().equals(category) && budget.getMonth().equals(month)) {
                return Optional.of(budget);
            }
        }
        return Optional.empty();
    }
}
