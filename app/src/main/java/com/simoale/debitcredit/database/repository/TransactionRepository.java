package com.simoale.debitcredit.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.TransactionDAO;
import com.simoale.debitcredit.model.Transaction;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionRepository {
    private TransactionDAO transactionDAO;
    private LiveData<List<Transaction>> transactionList;
    private WalletRepository walletRepository;
    private BudgetRepository budgetRepository;

    public TransactionRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        this.walletRepository = new WalletRepository(application);
        this.budgetRepository = new BudgetRepository(application);
        transactionDAO = db.TransactionDAO();
        transactionList = transactionDAO.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

    public LiveData<List<Transaction>> getTransactionList(int walletIdFrom, String description, String dateFrom, String dateTo, String categoryName, List<String> tags) {
        if (tags == null || tags.size() == 0) {
            return transactionDAO.getTransactions(walletIdFrom, description == null ? null : "%" + description + "%", dateFrom, dateTo, categoryName);
        } else {
            return transactionDAO.getTransactions(walletIdFrom, description == null ? null : "%" + description + "%", dateFrom, dateTo, categoryName, tags.toArray(new String[0]));
        }
    }

    public long addTransaction(final Transaction transaction) {
        AtomicLong result = new AtomicLong();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    result.set(transactionDAO.addTransaction(transaction));
                }
            }
        };
        DatabaseInstance.databaseWriteExecutor.execute(r);
        // TODO find a better way (Callable return something?!?!)
        try {
            DatabaseInstance.databaseWriteExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.walletRepository.updateBalance(transaction.getWalletIdFrom(), transaction.getAmount());
        Log.e("viewmodel trans", transaction.getAmount() + "");
        this.budgetRepository.updateBalance(transaction.getCategoryName(), transaction.getAmount());
        return result.get();
    }

    public Integer getBudgetSpent(String budgetCategoryName, String lastBudgetUpdate) {
        Integer result = transactionDAO.getBudgetSpent(budgetCategoryName, lastBudgetUpdate);
        return result == null ? 0 : result;
    }

}