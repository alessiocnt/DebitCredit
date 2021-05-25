package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.TagDAO;
import com.simoale.debitcredit.database.TransactionTagCrossRefDAO;
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.model.TransactionTagCrossRef;

import java.util.List;

public class TransactionTagCrossRefRepository {
    private TransactionTagCrossRefDAO transactionTagDAO;
    private LiveData<List<TransactionTagCrossRef>> transactionTagList;

    public TransactionTagCrossRefRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        this.transactionTagDAO = db.TransactionTagCrossRefDAO();
        this.transactionTagList = transactionTagDAO.getTransactionTag();
    }

    public LiveData<List<TransactionTagCrossRef>> getTransactionTag() {
        return transactionTagList;
    }

    public void addTransactionTag(final TransactionTagCrossRef transactionTag) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> this.transactionTagDAO.addTransactionTag(transactionTag));
    }

    public void addTransactionTags(final TransactionTagCrossRef[] transactionTags) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> this.transactionTagDAO.addTransactionTags(transactionTags));
    }

}
