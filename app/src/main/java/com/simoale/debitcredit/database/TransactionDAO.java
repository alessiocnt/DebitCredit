package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.simoale.debitcredit.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addTransaction(Transaction transaction);

    @androidx.room.Transaction
    @Query("SELECT * from `transaction` ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getTransactions();

    @androidx.room.Transaction
    @Query("SELECT DISTINCT * from `transaction`" +
            "WHERE (:walletIdFrom IS NULL OR transaction_wallet_id_from = :walletIdFrom) " +
            "AND (:description IS NULL OR transaction_description LIKE :description)" +
            "AND (:dateFrom IS NULL OR :dateTo IS NULL OR transaction_date BETWEEN :dateFrom AND :dateTo)" +
            "AND (:category IS NULL OR :category = 0 OR transaction_category_name = :category)" +
            "ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getTransactions(int walletIdFrom, String description, String dateFrom, String dateTo, String category);

    @androidx.room.Transaction
    @Query("SELECT DISTINCT * from `transaction` JOIN transaction_tag ON `transaction`.transaction_id = transaction_tag.transaction_id " +
            "WHERE (:walletIdFrom IS NULL OR transaction_wallet_id_from = :walletIdFrom)" +
            "AND (:description IS NULL OR transaction_description LIKE :description)" +
            "AND (:dateFrom IS NULL OR :dateTo IS NULL OR transaction_date BETWEEN :dateFrom AND :dateTo)" +
            "AND (:category IS NULL OR :category = 0 OR transaction_category_name = :category)" +
            "AND transaction_tag.tag_name IN (:tags)" +
            "ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getTransactions(int walletIdFrom, String description, String dateFrom, String dateTo, String category, String[] tags);

    @androidx.room.Transaction
    @Query("SELECT sum(transaction_amount) " +
            "from `transaction` " +
            "where transaction_category_name = :budgetCategoryName " +
            "AND transaction_date >= :lastBudgetUpdate " +
            "AND transaction_amount < 0 " +
            "GROUP BY transaction_category_name")
    Integer getBudgetSpent(String budgetCategoryName, String lastBudgetUpdate);

    @androidx.room.Transaction
    @Query("SELECT * from `transaction` " +
            "WHERE (transaction_date BETWEEN :dateSelectedFrom AND :dateSelectedTo)" +
            "ORDER BY transaction_date, transaction_category_name")
    LiveData<List<Transaction>> getTransactionsFromPeriod(String dateSelectedFrom, String dateSelectedTo);

    @Delete
    void deleteTransaction(Transaction transaction);

}
