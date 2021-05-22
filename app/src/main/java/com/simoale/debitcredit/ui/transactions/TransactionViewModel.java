package com.simoale.debitcredit.ui.transactions;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.TransactionRepository;
import com.simoale.debitcredit.model.Transaction;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository repository;

    private LiveData<List<Transaction>> transactionList;
    private final MutableLiveData<Transaction> transactionSelected = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> imageBitmpap = new MutableLiveData<>();

    public TransactionViewModel(Application application) {
        super(application);
        repository = new TransactionRepository(application);
        transactionList = repository.getTransactionList();
    }

    public void addTransaction(Transaction transaction) {
        repository.addTransaction(transaction);
    }

    public LiveData<List<Transaction>> getTransactionList() {
        return transactionList;
    }

    public LiveData<List<Transaction>> getTransactionList(@Nullable int walletIdFrom, @Nullable int walletIdTo, @Nullable String dateFrom, @Nullable String dateTo, @Nullable String category, @Nullable int[] tags) {
        return repository.getTransactionList(walletIdFrom, walletIdTo, dateFrom, dateTo, category, tags);
    }

    public Transaction getTransaction(int position) {
        return transactionList.getValue() == null ? null : transactionList.getValue().get(position);
    }

    public void select(Transaction transaction) {
        transactionSelected.setValue(transaction);
    }

    public LiveData<Transaction> getSelected() {
        return transactionSelected;
    }

    public void setImageBitmpap(Bitmap bitmpap) {
        imageBitmpap.setValue(bitmpap);
    }

    public LiveData<Bitmap> getBitmap() {
        return imageBitmpap;
    }
}






