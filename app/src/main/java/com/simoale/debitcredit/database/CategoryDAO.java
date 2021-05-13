package com.simoale.debitcredit.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.simoale.debitcredit.model.Category;

@Dao
public interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addCategory(Category category);

//    @Transaction
//    @Query("SELECT * from category ORDER BY category_name DESC")
//    LiveData<List<Wallet>> getCategories();
}
