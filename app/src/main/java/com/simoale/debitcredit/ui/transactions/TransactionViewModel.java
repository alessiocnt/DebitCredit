package com.simoale.debitcredit.ui.transactions;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.TransactionRepository;
import com.simoale.debitcredit.database.repository.WalletRepository;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository repository;

    private LiveData<List<Transaction>> transactionList;
    private final MutableLiveData<Transaction> transactionSelected = new MutableLiveData<>();

    public TransactionViewModel(Application application) {
        super(application);
        repository = new TransactionRepository(application);
        transactionList = repository.getTransactionList();
    }

    public void addTransaction(Transaction transaction){
        repository.addTransaction(transaction);
    }

    public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

    public Transaction getTransaction(int position){
        return transactionList.getValue() == null ? null : transactionList.getValue().get(position);
    }

    public void select(Transaction transaction) {
        transactionSelected.setValue(transaction);
    }

    public LiveData<Transaction> getSelected() {
        return transactionSelected;
    }
}






