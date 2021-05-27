package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Payee;

import java.util.List;

@Dao
public interface PayeeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addPayee(Payee payee);

    @Transaction
    @Query("SELECT * from payee ORDER BY payee_name")
    LiveData<List<Payee>> getPayees();

    @Transaction
    @Query("UPDATE payee SET payee_name = :newPayee WHERE payee_name = :oldPayee")
    void editPayee(String oldPayee, String newPayee);
}
