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
    @Query("SELECT SUM(transaction_amount) " +
            "from `transaction` " +
            "where transaction_category_id = :catId " +
            "and transaction_date >= :lastBudgetRefresh " +
            "GROUP BY transaction_category_id")
    Integer getBudgetSpent(int catId, Date lastBudgetRefresh);
}
