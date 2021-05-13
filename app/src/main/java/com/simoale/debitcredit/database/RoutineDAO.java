package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Routine;

import java.util.List;

@Dao
public interface RoutineDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addRoutine(Routine routine);

    @Transaction
    @Query("SELECT * from routine ORDER BY routine_name DESC")
    LiveData<List<Routine>> getRoutines();
}
