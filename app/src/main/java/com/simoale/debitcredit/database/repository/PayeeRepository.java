package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.PayeeDAO;
import com.simoale.debitcredit.model.Payee;

import java.util.List;

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
}