package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.RoutineDAO;
import com.simoale.debitcredit.model.Routine;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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

    public long addRoutine(final Routine routine) {
        AtomicLong result = new AtomicLong();
        DatabaseInstance.databaseWriteExecutor.execute(() -> result.set(routineDAO.addRoutine(routine)));
        try {
            DatabaseInstance.databaseWriteExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    public void updateRoutineDates(String lastUpdate, String nextUpdate, int id) {
        routineDAO.updateRoutineDates(lastUpdate, nextUpdate, id);
    }
}
