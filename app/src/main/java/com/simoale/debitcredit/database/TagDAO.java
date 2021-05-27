package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Tag;

import java.util.List;

@Dao
public interface TagDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addTag(Tag tag);

    @Transaction
    @Query("SELECT * from tag ORDER BY tag_name")
    LiveData<List<Tag>> getTags();

    @Transaction
    @Query("UPDATE tag SET tag_name = :newTag WHERE tag_name = :oldTag")
    void editTag(String oldTag, String newTag);

    @Delete
    void deleteTag(Tag tag);
}
