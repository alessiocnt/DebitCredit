package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.PayeeDAO;
import com.simoale.debitcredit.model.Payee;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PayeeRepository {
    private PayeeDAO payeeDAO;
    private LiveData<List<Payee>> payeeList;

    public PayeeRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        payeeDAO = db.PayeeDAO();
        payeeList = payeeDAO.getPayees();
    }

    public LiveData<List<Payee>> getPayeeList() {
        return payeeList;
    }

    public void addPayee(final Payee payee) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> payeeDAO.addPayee(payee));
    }

    public void editPayee(Payee oldPayee, Payee newPayee) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> payeeDAO.editPayee(oldPayee.getName(), newPayee.getName()));
    }

    public boolean deletePayee(Payee payee) {
        AtomicBoolean ret = new AtomicBoolean(true);
        DatabaseInstance.databaseWriteExecutor.execute(() -> {
            try {
                payeeDAO.deletePayee(payee);
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