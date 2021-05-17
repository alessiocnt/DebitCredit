package com.simoale.debitcredit.ui.routine;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.RoutineRepository;
import com.simoale.debitcredit.model.Routine;

import java.util.List;

public class RoutineViewModel extends AndroidViewModel {

    private final MutableLiveData<Routine> routineSelected = new MutableLiveData<>();
    private RoutineRepository repository;
    private LiveData<List<Routine>> routineList;

    public RoutineViewModel(Application application) {
        super(application);
        repository = new RoutineRepository(application);
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
}