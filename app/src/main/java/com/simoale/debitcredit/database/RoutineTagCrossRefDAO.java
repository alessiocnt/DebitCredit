package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.RoutineTagCrossRef;

import java.util.List;

@Dao
public interface RoutineTagCrossRefDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addRoutineTag(RoutineTagCrossRef routineTagCrossRef);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addRoutineTags(RoutineTagCrossRef... routineTagCrossRefs);

    @Transaction
    @Query("SELECT * from routine_tag")
    LiveData<List<RoutineTagCrossRef>> getRoutineTag();

    @Transaction
    @Query("SELECT tag_name FROM routine_tag WHERE routine_id = :routineId")
    LiveData<List<String>> getTagsOfRoutine(int routineId);
}
