package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.TransactionTagCrossRef;

import java.util.List;

@Dao
public interface TransactionTagCrossRefDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addTransactionTag(TransactionTagCrossRef transactionTag);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addTransactionTags(TransactionTagCrossRef... transactionTag);

    @Transaction
    @Query("SELECT * from transaction_tag")
    LiveData<List<TransactionTagCrossRef>> getTransactionTag();

    @Transaction
    @Query("SELECT tag_name FROM transaction_tag WHERE transaction_id = :transactionId")
    LiveData<List<String>> getTagsOfTransaction(int transactionId);
}