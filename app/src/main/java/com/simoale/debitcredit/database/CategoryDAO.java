package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addCategory(Category category);

    @Transaction
    @Query("SELECT * from category ORDER BY category_name")
    LiveData<List<Category>> getCategories();
}
