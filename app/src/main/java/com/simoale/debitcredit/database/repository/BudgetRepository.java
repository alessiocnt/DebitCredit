package com.simoale.debitcredit.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.BudgetDAO;
import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.model.Budget;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BudgetRepository {
    private BudgetDAO budgetDAO;
    private LiveData<List<Budget>> budgetList;

    public BudgetRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        budgetDAO = db.BudgetDAO();
        budgetList = budgetDAO.getBudgets();
    }

    public LiveData<List<Budget>> getBudgetList() {
        return budgetList;
    }

    public void addBudget(final Budget budget) {
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                budgetDAO.addBudget(budget);
            }
        });
    }

    public void updateBudgetDates(String lastUpdate, String nextUpdate, int id) {
        budgetDAO.updateBudgetDates(lastUpdate, nextUpdate, id);
    }

    public void updateBalance(String categoryName, float amount) {

        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                budgetDAO.updateBalance(categoryName, amount);
            }
        });
    }

    public boolean deleteBudget(Budget budget) {
        AtomicBoolean ret = new AtomicBoolean(true);
        DatabaseInstance.databaseWriteExecutor.execute(() -> {
            try {
                budgetDAO.deleteBudget(budget);
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