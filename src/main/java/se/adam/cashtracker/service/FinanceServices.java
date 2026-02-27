package se.adam.cashtracker.service;

import se.adam.cashtracker.model.Category;
import se.adam.cashtracker.model.Transaction;
import se.adam.cashtracker.model.TransactionType;
import se.adam.cashtracker.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

public class FinanceServices {
    private final TransactionRepository repository;

    public FinanceServices(TransactionRepository repository) {
        this.repository = repository;
    }

    public void addTransaction(Transaction transaction) {
        List<Transaction> transactions = repository.findAll();

        if (transactions.stream().anyMatch(
                t -> t.getId().equals(transaction.getId()))) {
            throw new RuntimeException("Duplicate ID detected");
        }
        if (transactions.stream().anyMatch(
                t -> t.getReference().equals(transaction.getReference()))) {
            throw new RuntimeException("Duplicate reference detected");
        }

        transactions.add(transaction);
        repository.saveAll(transactions);
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public BigDecimal getSpendingByCategory(Category category) {
        return getSpendingByCategory().getOrDefault(category, BigDecimal.ZERO);
    }

    public Map<Category, BigDecimal> getSpendingByCategory() {
        Map<Category, BigDecimal> spendings = new EnumMap<>(Category.class);
        List<Transaction> transactions = repository.findAll();

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
        List<Transaction> transactions = repository.findAll();
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
        List<Transaction> transactions = repository.findAll();

        if (!transactions.removeIf(t -> t.getReference().equals(reference))) {
            throw new IllegalArgumentException(
                    "No transaction found with reference: " + reference
            );
        }

        repository.saveAll(transactions);

        System.out.println("Successfully deleted receipt with reference " + reference);
    }
}
