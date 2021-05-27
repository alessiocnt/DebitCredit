package com.simoale.debitcredit.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.BudgetDAO;
import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.WalletDAO;
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

public class BudgetRepository {
    private BudgetDAO budgetDAO;
    private LiveData<List<Budget>> budgetList;

    public BudgetRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        budgetDAO = db.BudgetDAO();
        budgetList = budgetDAO.getBudgets();
    }

    public LiveData<List<Budget>> getBudgetList(){
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
                Log.e("adsfsdfgasdgadfgdasfg", ""+ categoryName + " " + amount);
                budgetDAO.updateBalance(categoryName, amount);
            }
        });
    }
}