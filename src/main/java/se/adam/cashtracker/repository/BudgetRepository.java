package se.adam.cashtracker.repository;

import se.adam.cashtracker.model.Category;
import se.adam.cashtracker.model.MonthlyBudget;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository {
    void save(MonthlyBudget budget);

    Optional<MonthlyBudget> findByCategoryAndMonth(Category category, YearMonth month);

    List<MonthlyBudget> findAll();
}
