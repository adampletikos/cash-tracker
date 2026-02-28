package se.adam.cashtracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BudgetComparison {
    private final BigDecimal budget;
    private final BigDecimal spent;

    public BudgetComparison(BigDecimal budget, BigDecimal spent) {
        if (budget == null || spent == null) {
            throw new IllegalArgumentException("Budget or Spent cannot be null");
        }
        if (budget.compareTo(BigDecimal.ZERO) < 0 || spent.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget or Spent cannot be negative");
        }
        this.budget = budget;
        this.spent = spent;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public boolean isOverBudget() {
        return spent.compareTo(budget) > 0;
    }

    public BigDecimal getRemaining() {
        return budget.subtract(spent);
    }

    public double getPercentageUsed() {
        if (budget.compareTo(spent) == 0) {
            return 100.0;
        }
        else return spent
                .divide(budget, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}


