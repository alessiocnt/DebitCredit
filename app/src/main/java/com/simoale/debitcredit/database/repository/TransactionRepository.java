package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.TransactionDAO;
import com.simoale.debitcredit.model.Transaction;

import java.util.Date;
import java.util.List;

public class TransactionRepository {
    private TransactionDAO transactionDAO;
    private LiveData<List<Transaction>> transactionList;

    public TransactionRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        transactionDAO = db.TransactionDAO();
        transactionList = transactionDAO.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactionList(){
        return transactionList;
    }

    public void addTransaction(final Transaction transaction) {
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                transactionDAO.addTransaction(transaction);
            }
        });
    }

    public Integer getBudgetSpent(int budgetCategoryId, String lastBudgetUpdate){
        Integer result = transactionDAO.getBudgetSpent(budgetCategoryId, lastBudgetUpdate);
        return result == null ? 0 : result;
    }

}