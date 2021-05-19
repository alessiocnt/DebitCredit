package com.simoale.debitcredit.database.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.CategoryDAO;
import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.model.Category;

import java.util.List;

public class CategoryRepository {
    private CategoryDAO categoryDAO;
    private LiveData<List<Category>> categoryList;

    public CategoryRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        categoryDAO = db.CategoryDAO();
        categoryList = categoryDAO.getCategories();
    }

    public LiveData<List<Category>> getCategoryList(){
        return categoryList;
    }

    public void addCategory(final Category category) {
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.addCategory(category);
            }
        });
    }
}