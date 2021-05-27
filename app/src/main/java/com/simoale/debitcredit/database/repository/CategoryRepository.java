package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.CategoryDAO;
import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.model.Category;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CategoryRepository {
    private CategoryDAO categoryDAO;
    private LiveData<List<Category>> categoryList;
    private Application application;

    public CategoryRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        this.application = application;
        categoryDAO = db.CategoryDAO();
        categoryList = categoryDAO.getCategories();
    }

    public LiveData<List<Category>> getCategoryList() {
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

    public void editCategory(Category oldCat, Category newCat) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> categoryDAO.editCategory(oldCat.getName(), newCat.getName()));
    }

    public boolean deleteCategory(Category category) {
        AtomicBoolean ret = new AtomicBoolean(true);
        DatabaseInstance.databaseWriteExecutor.execute(() -> {
            try {
                categoryDAO.deleteCategory(category);
            } catch (Exception e) {
                ret.set(false);
            }
        });
        try {
            DatabaseInstance.databaseWriteExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret.get();
    }
}