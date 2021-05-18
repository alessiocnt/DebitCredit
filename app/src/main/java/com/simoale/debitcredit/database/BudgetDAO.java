package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Budget;

import java.util.List;

@Dao
public interface BudgetDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addBudget(Budget budget);

    @Transaction
    @Query("SELECT * from budget ORDER BY budget_name DESC")
    LiveData<List<Budget>> getBudgets();

    @Transaction
    @Query("UPDATE budget " +
            "SET budget_last_update = :lastUpdate, budget_next_update = :nextUpdate " +
            "WHERE budget_id = :id")
    void updateBudgetDates(String lastUpdate, String nextUpdate, int id);
}
