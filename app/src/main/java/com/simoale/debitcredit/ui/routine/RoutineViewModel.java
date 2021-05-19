package com.simoale.debitcredit.ui.routine;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.RoutineRepository;
import com.simoale.debitcredit.database.repository.TransactionRepository;
import com.simoale.debitcredit.model.Interval;
import com.simoale.debitcredit.model.Routine;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.utils.Utilities;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoutineViewModel extends AndroidViewModel {

    private final MutableLiveData<Routine> routineSelected = new MutableLiveData<>();
    private RoutineRepository repository;
    private TransactionRepository transactionRepository;
    private LiveData<List<Routine>> routineList;

    public RoutineViewModel(Application application) {
        super(application);
        repository = new RoutineRepository(application);
        transactionRepository = new TransactionRepository(application);
        routineList = repository.getRoutineList();
    }

    public void addRoutine(Routine routine) {
        repository.addRoutine(routine);
    }

    public LiveData<List<Routine>> getRoutineList() {
        return routineList;
    }

    public Routine getRoutine(int position) {
        return routineList.getValue() == null ? null : routineList.getValue().get(position);
    }

    public void select(Routine routine) {
        routineSelected.setValue(routine);
    }

    public LiveData<Routine> getSelected() {
        return routineSelected;
    }

    public void update(List<Routine> routines) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (Routine routine : routines) {
                Pair<String, String> dates = updateRoutineDates(routine);
                repository.updateRoutineDates(dates.first, dates.second, routine.getId());
            }
        });

    }

    private Pair<String, String> updateRoutineDates(Routine routine) {
        String dateLastUpdate = routine.getDateLastUpdate();
        String dateNextUpdate = routine.getDateNextUpdate();
        int repeatNumber = routine.getRepeatNumber();
        String repeatInterval = routine.getRepeatInterval();
        long today = Calendar.getInstance().getTime().getTime();
        long last = Utilities.getDateFromString(dateLastUpdate).getTime();
        long diff = today - last;
        long dayDiff = diff / (1000 * 60 * 60 * 24);
        Calendar lastUpdate = Calendar.getInstance();
        Calendar nextUpdate = Calendar.getInstance();
        // devo aggiornare ogni repeatNumber*repeatInterval
        int daysBetweenUpdates = repeatNumber * Interval.valueOf(repeatInterval.toUpperCase()).daysNumber;
        long numberOfUpdates = dayDiff / daysBetweenUpdates;
        Log.e("Routine number of updates", numberOfUpdates + "");
        if (numberOfUpdates != 0) {
            lastUpdate.setTime(Utilities.getDateFromString(dateLastUpdate));
            lastUpdate.add(Calendar.DAY_OF_MONTH, (int) (numberOfUpdates * daysBetweenUpdates));
            dateLastUpdate = Utilities.getStringFromDate(lastUpdate.getTime());
            Log.e("LastUpdate", dateLastUpdate);
            nextUpdate.setTime(Utilities.getDateFromString(dateLastUpdate));
            nextUpdate.add(Calendar.DAY_OF_MONTH, daysBetweenUpdates);
            dateNextUpdate = Utilities.getStringFromDate(nextUpdate.getTime());
            Log.e("NextUpdate", dateNextUpdate);
        }

        while (numberOfUpdates > 0) {
            numberOfUpdates--;
            nextUpdate.add(Calendar.DAY_OF_MONTH, -daysBetweenUpdates);
            // TODO fix routine payee
            this.transactionRepository.addTransaction(new Transaction(routine.getAmount(),
                    "Transaction from routine: " + routine.getName(), routine.getCategoryId(),
                    /*routine.getPayeeId()*/ 1, Utilities.getStringFromDate(nextUpdate.getTime()),
                    routine.getWalletId(), routine.getWalletId(), null, null, null));
        }
        return new Pair<>(dateLastUpdate, dateNextUpdate);
    }
}