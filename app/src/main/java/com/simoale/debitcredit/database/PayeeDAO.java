package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Payee;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

@Dao
public interface PayeeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addCategory(Payee payee);

    @Transaction
    @Query("SELECT * from payee ORDER BY payee_name DESC")
    LiveData<List<Wallet>> getCategories();
}
