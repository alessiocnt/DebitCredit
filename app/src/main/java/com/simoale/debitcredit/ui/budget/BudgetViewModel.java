package com.simoale.debitcredit.ui.budget;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.BudgetRepository;
import com.simoale.debitcredit.database.repository.TransactionRepository;
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.model.Interval;
import com.simoale.debitcredit.utils.Utilities;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetViewModel extends AndroidViewModel {

    // The budget currently selected
    private final MutableLiveData<Budget> budgetSelected = new MutableLiveData<>();
    private BudgetRepository repository;
    // The budget list
    private LiveData<List<Budget>> budgetList;

    public BudgetViewModel(Application application) {
        super(application);
        repository = new BudgetRepository(application);
        budgetList = repository.getBudgetList();
    }

    public void addBudget(Budget budget) {
        repository.addBudget(budget);
    }

    public void updateBudgetDates(String lastUpdate, String nextUpdate, int id) {
        repository.updateBudgetDates(lastUpdate, nextUpdate, id);
    }

    public LiveData<List<Budget>> getBudgetList() {
        return budgetList;
    }

    public Budget getBudget(int position) {
        return budgetList.getValue() == null ? null : budgetList.getValue().get(position);
    }

    public void select(Budget budget) {
        budgetSelected.setValue(budget);
    }

    public LiveData<Budget> getSelected() {
        return budgetSelected;
    }

    public void update(List<Budget> budgets) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (Budget budget : budgets) {
                Pair<String, String> dates = updateBudgetDates(budget.getDateLastUpdate(), budget.getDateNextUpdate(), budget.getRepeatNumber(), budget.getRepeatInterval());
                repository.updateBudgetDates(dates.first, dates.second, budget.getId());
            }
        });

    }

    private Pair<String, String> updateBudgetDates(String dateLastUpdate, String dateNextUpdate, int repeatNumber, String repeatInterval) {
        long today = Calendar.getInstance().getTime().getTime();
        long last = Utilities.getDateFromString(dateLastUpdate).getTime();
        long diff = today - last;
        long dayDiff = diff / (1000 * 60 * 60 * 24);
        // devo aggiornare ogni repeatNumber*repeatInterval
        int daysBetweenUpdates = repeatNumber * Interval.valueOf(repeatInterval.toUpperCase()).daysNumber;
        long numberOfUpdates = dayDiff / daysBetweenUpdates;
        if (numberOfUpdates != 0) {
            Calendar lastUpdate = Calendar.getInstance();
            lastUpdate.setTime(Utilities.getDateFromString(dateLastUpdate));
            lastUpdate.add(Calendar.DAY_OF_MONTH, (int) (numberOfUpdates * daysBetweenUpdates));
            dateLastUpdate = Utilities.getStringFromDate(lastUpdate.getTime());
            Calendar nextUpdate = Calendar.getInstance();
            nextUpdate.setTime(Utilities.getDateFromString(dateLastUpdate));
            nextUpdate.add(Calendar.DAY_OF_MONTH, daysBetweenUpdates);
            dateNextUpdate = Utilities.getStringFromDate(nextUpdate.getTime());
        }
        return new Pair<>(dateLastUpdate, dateNextUpdate);
    }

    public Map<String, Integer> calculateBudgetsLeftover(List<Budget> budgets) {
        TransactionRepository transactionRepository = new TransactionRepository(getApplication());
        Map<String, Integer> budgetLeftover = new HashMap<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (Budget budget : budgets) {
                budgetLeftover.put(budget.getName(),
                        (int) (100 - (transactionRepository.getBudgetSpent(budget.getCategoryName(), budget.getDateLastUpdate()) / budget.getLimit()) * 100));
            }
        });
        return budgetLeftover;
    }
}






