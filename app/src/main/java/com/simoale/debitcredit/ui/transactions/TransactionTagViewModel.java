package com.simoale.debitcredit.ui.transactions;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.TransactionTagCrossRefRepository;
import com.simoale.debitcredit.model.TransactionTagCrossRef;

import java.util.List;

public class TransactionTagViewModel extends AndroidViewModel {

    private TransactionTagCrossRefRepository repository;

    private LiveData<List<TransactionTagCrossRef>> transactionTagList;
    private final MutableLiveData<List<TransactionTagCrossRef>> transactionTagSelected = new MutableLiveData<>();

    public TransactionTagViewModel(Application application) {
        super(application);
        repository = new TransactionTagCrossRefRepository(application);
        transactionTagList = repository.getTransactionTag();
    }

    public void addTransactionTag(TransactionTagCrossRef transactionTag) {
        repository.addTransactionTag(transactionTag);
    }

    public LiveData<List<TransactionTagCrossRef>> getTransactionTagList() {
        return transactionTagList;
    }

    public TransactionTagCrossRef getTransactionTag(int position) {
        return transactionTagList.getValue() == null ? null : transactionTagList.getValue().get(position);
    }

    public void select(List<TransactionTagCrossRef> transactionTag) {
        transactionTagSelected.setValue(transactionTag);
    }

    public LiveData<List<TransactionTagCrossRef>> getSelected() {
        return transactionTagSelected;
    }
}






