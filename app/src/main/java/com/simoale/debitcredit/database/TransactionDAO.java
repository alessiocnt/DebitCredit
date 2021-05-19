package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.simoale.debitcredit.model.Transaction;

import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addTransaction(Transaction transaction);

    @androidx.room.Transaction
    @Query("SELECT * from `transaction` ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getTransactions();

    @androidx.room.Transaction
    @Query("SELECT COALESCE(sum(transaction_amount), 0) " +
            "from `transaction` " +
            "where transaction_category_id = :budgetCategoryId " +
            "and transaction_date >= :lastBudgetUpdate " +
            "GROUP BY transaction_category_id")
    Integer getBudgetSpent(int budgetCategoryId, String lastBudgetUpdate);
}
