package com.simoale.debitcredit.ui.budget;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.BudgetRepository;
import com.simoale.debitcredit.database.repository.WalletRepository;
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

public class BudgetViewModel extends AndroidViewModel {

    private BudgetRepository repository;

    // The budget list
    private LiveData<List<Budget>> budgetList;
    // The budget currently selected
    private final MutableLiveData<Budget> budgetSelected = new MutableLiveData<>();

    public BudgetViewModel(Application application) {
        super(application);
        repository = new BudgetRepository(application);
        budgetList = repository.getBudgetList();
    }

    public void addBudget(Budget budget){
        repository.addBudget(budget);
    }

    public LiveData<List<Budget>> getBudgetList() {
        return budgetList;
    }

    public Budget getBudget(int position){
        return budgetList.getValue() == null ? null : budgetList.getValue().get(position);
    }

    public void select(Budget budget) {
        budgetSelected.setValue(budget);
    }

    public LiveData<Budget> getSelected() {
        return budgetSelected;
    }
}






