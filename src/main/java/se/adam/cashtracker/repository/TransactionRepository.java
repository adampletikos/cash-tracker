package se.adam.cashtracker.repository;

import se.adam.cashtracker.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    void saveAll(List<Transaction> transactions);

    List<Transaction> findAll();
}
