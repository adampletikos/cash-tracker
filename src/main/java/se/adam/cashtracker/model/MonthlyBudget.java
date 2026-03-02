package se.adam.cashtracker.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.Locale;
import java.util.Objects;

public class MonthlyBudget {
    private final YearMonth month;
    private final BigDecimal amount;
    private final Category category;

    public MonthlyBudget(
            YearMonth month,
            BigDecimal amount,
            Category category
    ) {
        if (month == null) {throw new IllegalArgumentException("wrong format for month");}
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {throw new IllegalArgumentException("wrong format for amoiunt");}
        if (category == null) {throw new IllegalArgumentException("wrong format for categoru");}

        this.month = month;
        this.amount = amount;
        this.category = category;
    }

    public boolean isZeroBudget() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public YearMonth getMonth() {
        return month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof MonthlyBudget)) {return false;}
        MonthlyBudget that = (MonthlyBudget) o;
        return month.equals(that.month) && category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, category);
    }

    public String toCsv() {
        return month + "," +
                amount + "," +
                category;
    }

    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("sv", "SE"));
    public String formatted() {
        return month + " | " +
                category + " | " +
                formatter.format(amount) + "kr";
    }
}
