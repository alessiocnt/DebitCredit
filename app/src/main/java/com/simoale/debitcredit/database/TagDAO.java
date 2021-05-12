package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

@Dao
public interface TagDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addTag(Tag tag);

    @Transaction
    @Query("SELECT * from tag ORDER BY tag_name DESC")
    LiveData<List<Tag>> getTags();
}
