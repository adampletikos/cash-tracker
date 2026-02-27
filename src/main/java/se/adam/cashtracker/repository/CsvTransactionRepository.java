package se.adam.cashtracker.repository;

import se.adam.cashtracker.model.Category;
import se.adam.cashtracker.model.Transaction;
import se.adam.cashtracker.model.TransactionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedWriter;

public class CsvTransactionRepository implements TransactionRepository {
    private final Path filePath;

    public CsvTransactionRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("id,date,type,category,amount,description");
            writer.newLine();
            for (Transaction trans : transactions) {
                writer.write(trans.toCsv());
                writer.newLine();
            }
        } catch (IOException e ) {
            throw new RuntimeException("Failed to save transactions", e);
        }
    }

    @Override public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        Set<UUID> seenIds = new HashSet<>();

        if (!Files.exists(filePath)) {
            return transactions;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] transaction = line.split(",");

                if (line.isBlank()) {
                    continue;
                }

                UUID id = UUID.fromString(transaction[0]);

                if (!seenIds.add(id)) {
                    throw new IllegalStateException("Duplicate ID detected in file: " + id);
                }

                transactions.add(new Transaction(
                        UUID.fromString(transaction[0]),
                        LocalDate.parse(transaction[1]),
                        TransactionType.valueOf(transaction[2]),
                        Category.valueOf(transaction[3]),
                        new BigDecimal(transaction[4]),
                        transaction[5],
                        transaction[6]
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read transaction",e);
        } return transactions;
    }
}
