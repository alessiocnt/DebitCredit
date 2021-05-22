package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.simoale.debitcredit.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addTransaction(Transaction transaction);

    @androidx.room.Transaction
    @Query("SELECT * from `transaction` ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getTransactions();

    @androidx.room.Transaction
    @Query("SELECT DISTINCT * from `transaction` " +
            "WHERE (:walletIdFrom IS NULL OR transaction_wallet_id_from = :walletIdFrom)" +
            "AND (:description IS NULL OR transaction_description LIKE :description)" +
            "AND (:walletIdTo IS NULL OR transaction_wallet_id_to = :walletIdTo)" +
            "AND (:dateFrom IS NULL OR :dateTo IS NULL OR transaction_date BETWEEN :dateFrom AND :dateTo)" +
            "AND (:category IS NULL OR :category = 0 OR transaction_category_name = :category)")
//            "AND (:tags IS NULL OR transaction_tags IN tags)")
    LiveData<List<Transaction>> getTransactions(int walletIdFrom, int walletIdTo, String description, String dateFrom, String dateTo, String category); // int[] tags missing

    @androidx.room.Transaction
    @Query("SELECT sum(transaction_amount) " +
            "from `transaction` " +
            "where transaction_category_name = :budgetCategoryName " +
            "and transaction_date >= :lastBudgetUpdate " +
            "GROUP BY transaction_category_name")
    Integer getBudgetSpent(String budgetCategoryName, String lastBudgetUpdate);
}
