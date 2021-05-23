package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.TransactionDAO;
import com.simoale.debitcredit.model.Transaction;

import java.util.List;

public class TransactionRepository {
    private TransactionDAO transactionDAO;
    private LiveData<List<Transaction>> transactionList;

    public TransactionRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        transactionDAO = db.TransactionDAO();
        transactionList = transactionDAO.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

    public LiveData<List<Transaction>> getTransactionList(int walletIdFrom, int walletIdTo, String description, String dateFrom, String dateTo, String categoryName, List<String> tags) {
        if (tags == null || tags.size() == 0) {
            return transactionDAO.getTransactions(walletIdFrom, walletIdTo, description == null ? null : "%" + description + "%", dateFrom, dateTo, categoryName);
        } else {
            return transactionDAO.getTransactions(walletIdFrom, walletIdTo, description == null ? null : "%" + description + "%", dateFrom, dateTo, categoryName, tags.toArray(new String[0]));
        }
    }

    public void addTransaction(final Transaction transaction) {
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                transactionDAO.addTransaction(transaction);
            }
        });
    }

    public Integer getBudgetSpent(String budgetCategoryName, String lastBudgetUpdate) {
        Integer result = transactionDAO.getBudgetSpent(budgetCategoryName, lastBudgetUpdate);
        return result == null ? 0 : result;
    }

}