package se.adam.cashtracker.service;

import se.adam.cashtracker.model.*;
import se.adam.cashtracker.repository.BudgetRepository;
import se.adam.cashtracker.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

public class FinanceServices {
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    public FinanceServices(TransactionRepository transactionRepository, BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    public void addTransaction(Transaction transaction) {
        List<Transaction> transactions = transactionRepository.findAll();

        if (transactions.stream().anyMatch(
                t -> t.getId().equals(transaction.getId()))) {
            throw new RuntimeException("Duplicate ID detected");
        }
        if (transactions.stream().anyMatch(
                t -> t.getReference().equals(transaction.getReference()))) {
            throw new RuntimeException("Duplicate reference detected");
        }

        transactions.add(transaction);
        transactionRepository.saveAll(transactions);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public BigDecimal getSpendingByCategory(Category category) {
        return getSpendingByCategory().getOrDefault(category, BigDecimal.ZERO);
    }

    public Map<Category, BigDecimal> getSpendingByCategory() {
        Map<Category, BigDecimal> spendings = new EnumMap<>(Category.class);
        List<Transaction> transactions = transactionRepository.findAll();

        for (Category categories: Category.values()) {
            spendings.put(categories, BigDecimal.ZERO);
        }

        for (Transaction t : transactions) {
            if (t.getType().equals(TransactionType.EXPENSE)) {
                spendings.merge(t.getCategory(), t.getAmount(), BigDecimal::add);
            }
        }

        return spendings;
    }

    public BigDecimal getSpendingByMonth(YearMonth month) {
        List<Transaction> transactions = transactionRepository.findAll();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (YearMonth.from(t.getDate()).equals(month) && t.getType().equals(TransactionType.EXPENSE)) {
                totalAmount = totalAmount.add(t.getAmount());
            }
        }
        return totalAmount;
    }

    public void deleteByReference(String reference) {
        Objects.requireNonNull(reference, "reference cannot be null");
        List<Transaction> transactions = transactionRepository.findAll();

        if (!transactions.removeIf(t -> t.getReference().equals(reference))) {
            throw new IllegalArgumentException(
                    "No transaction found with reference: " + reference
            );
        }

        transactionRepository.saveAll(transactions);

        System.out.println("Successfully deleted receipt with reference " + reference);
    }

    //Monthly budget
    public void addBudget(MonthlyBudget budget) {
        budgetRepository.save(budget);
    }

    public BudgetComparison compareToBudget(Category category, YearMonth month ) {
        MonthlyBudget budget = budgetRepository
                .findByCategoryAndMonth(category, month)
                .orElseThrow(() -> new IllegalArgumentException("No budget found"));

        BigDecimal spent = BigDecimal.ZERO;

        List<Transaction> transactions = transactionRepository.findAll();

        for (Transaction t : transactions) {
            boolean sameCategory = t.getCategory().equals(category);
            boolean sameMonth = YearMonth.from(t.getDate()).equals(month);
            boolean isExpense = t.getType() == TransactionType.EXPENSE;

            if (sameCategory && sameMonth && isExpense) {
                spent = spent.add(t.getAmount());
            }
        }

        return new BudgetComparison(budget.getAmount(), spent);
    }
}
