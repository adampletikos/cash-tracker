package se.adam.cashtracker.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final LocalDate date;
    private final TransactionType type;
    private final Category category;
    private final BigDecimal amount;
    private final String description;
    private final String reference;

    //Constructor for loading transactions from file, no random UUID created
    public Transaction(
            UUID id,
            LocalDate date,
            TransactionType type,
            Category category,
            BigDecimal amount,
            String description,
            String reference
    ) {
        validate(id, date, type, category, amount, description, reference);

        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.reference = reference;

    }

    //Constructor for creating new transactions, random UUID created.
    public Transaction(
            LocalDate date,
            TransactionType type,
            Category category,
            BigDecimal amount,
            String description,
            String reference
    ) {
        this(UUID.randomUUID(),date,type,category,amount,description,reference);
    }

    //validates the values when creating a new Transaction type. No null values.
    public static void validate(UUID id, LocalDate date, TransactionType type, Category category, BigDecimal amount, String description, String reference) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null");
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (category == null)
            throw new IllegalArgumentException("Category cannot be empty");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description must be provided");
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("Reference cannot be empty");
        }
    }

    //getters for all the inherent values.
    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", type=" + type +
                ", category=" + category +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }

    public String toCsv() {
        return id + "," +
                date + "," +
                type + "," +
                category + "," +
                amount + "," +
                description + "," +
                reference;
    }

    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("sv", "SE"));
    public String formatted() {
        return description + ", " +
                date + ", " +
                formatter.format(amount) + "kr";
    }
}