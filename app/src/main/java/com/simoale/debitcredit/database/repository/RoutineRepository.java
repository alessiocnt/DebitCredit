package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.RoutineDAO;
import com.simoale.debitcredit.model.Routine;

import java.util.List;

public class RoutineRepository {
    private RoutineDAO routineDAO;
    private LiveData<List<Routine>> routineList;

    public RoutineRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        routineDAO = db.RoutineDAO();
        routineList = routineDAO.getRoutines();
    }

    public LiveData<List<Routine>> getRoutineList() {
        return routineList;
    }

    public void addRoutine(final Routine routine) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> routineDAO.addRoutine(routine));
    }

    public void updateRoutineDates(String lastUpdate, String nextUpdate, int id) {
        routineDAO.updateRoutineDates(lastUpdate, nextUpdate, id);
    }
}
