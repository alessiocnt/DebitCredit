package com.simoale.debitcredit.ui.payee;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.PayeeRepository;
import com.simoale.debitcredit.model.Payee;

import java.util.List;

public class PayeeViewModel extends AndroidViewModel {

    private PayeeRepository repository;

    private LiveData<List<Payee>> payeeList;
    private final MutableLiveData<Payee> payeeSelected = new MutableLiveData<>();

    public PayeeViewModel(Application application) {
        super(application);
        repository = new PayeeRepository(application);
        payeeList = repository.getPayeeList();
    }

    public void addPayee(Payee payee) {
        repository.addPayee(payee);
    }

    public LiveData<List<Payee>> getPayeeList() {
        return payeeList;
    }

    public Payee getPayee(int position) {
        return payeeList.getValue() == null ? null : payeeList.getValue().get(position);
    }

    public void select(Payee payee) {
        payeeSelected.setValue(payee);
    }

    public LiveData<Payee> getSelected() {
        return payeeSelected;
    }

    public void editPayee(Payee oldPayee, Payee newPayee) {
        repository.editPayee(oldPayee, newPayee);
    }

    public boolean deletePayee(Payee payee) {
        return repository.deletePayee(payee);
    }
}






