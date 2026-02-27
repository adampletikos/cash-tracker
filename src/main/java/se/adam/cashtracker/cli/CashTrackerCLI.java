package se.adam.cashtracker.cli;

import se.adam.cashtracker.model.Category;
import se.adam.cashtracker.model.Transaction;
import se.adam.cashtracker.model.TransactionType;
import se.adam.cashtracker.service.FinanceServices;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class CashTrackerCLI {
    private final FinanceServices service;
    private final Scanner scanner = new Scanner(System.in);
    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("sv", "SE"));


    public CashTrackerCLI(FinanceServices service) {
        this.service = service;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();

            String input = scanner.nextLine();

            switch (input) {
                case "1" : addTransaction(); break;
                case "2" : listTransactions(); break;
                case "3" : monthlySpending(); break;
                case "4": running = false; break;
                default : System.out.println("Not an option");
            }
        }
    }

    public void printMenu() {
        System.out.println("""
                ==== Cash Tracker ====
                1. Add transaction
                2. List transactions
                3. Show monthly spending
                4. Exit"""
        );
        System.out.print("Choose: ");
    }

    public void addTransaction() {
        String reference = readReference();
        BigDecimal amount = readAmount();
        LocalDate date = readDate();
        Category category = readCategory();
        TransactionType transactionType = readTransactionType();
        String description = readDescription();

        try {
            service.addTransaction(
                    new Transaction(
                            date,
                            transactionType,
                            category,
                            amount,
                            description,
                            reference
                    )
            );
            System.out.println("Added transaction successfully!");
        } catch (IllegalArgumentException e) {System.out.println("Error " + e.getMessage());}

    }

    public void listTransactions() {
        List<Transaction> transactions = service.getAllTransactions();
        for (Transaction t : transactions) {
            System.out.println(t.formatted());
        }
    }

    public void monthlySpending() {
        String input;
        while (true) {
            try {
                System.out.println("(YYYY-MM): ");
                input = scanner.nextLine();
                YearMonth month = YearMonth.parse(input.trim());

                BigDecimal total =
                        service.getSpendingByMonth(month);

                System.out.println("\nSpending for " + month + ": "
                        + formatter.format(total) + "kr");
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use YYYY-MM (example: 2026-03).");
            }
        }
    }

    private String readReference() {
        while (true) {
            System.out.print("Reference: ");
            String input = scanner.nextLine().trim();

            if (!input.isBlank()) {
                return input;
            }

            System.out.println("Reference cannot be empty. Try again.");
        }
    }

    private BigDecimal readAmount() {
        while (true) {
            try{
                System.out.print("Amount: ");
                BigDecimal amount =
                        new BigDecimal(scanner.nextLine().trim());
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    return amount;
                }
                System.out.println("Amount must be positive");
            } catch (Exception e) {
                System.out.println("Invalid amount please insert a decimal number.");
            }

        }
    }

    private LocalDate readDate() {
        while (true) {
            try{
                System.out.print("Date (YYYY-MM-DD): ");
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (Exception e) {System.out.println("Invalid date format. Try again.");}
        }
    }

    private Category readCategory() {
        while (true) {
            try{
                System.out.print("Category " + Arrays.toString(Category.values()) + ":");
                return Category.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (Exception e) {System.out.println("Invalid Category. Try again.");}
        }
    }

    private TransactionType readTransactionType() {
        while (true) {
            try{
                System.out.print("Transaction type (INCOME / EXPENSE): ");
                return TransactionType.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (Exception e) {System.out.println("Invalid Transaction type. Try again.");}
        }
    }

    private String readDescription() {
        boolean valid = false;
        String description = "";

        while (!valid) {
            System.out.print("Please enter a description: ");
            description = scanner.nextLine();
            if (!description.isBlank() && description.length() <= 30) {valid = true;}
        }

        return description;
    }
}
