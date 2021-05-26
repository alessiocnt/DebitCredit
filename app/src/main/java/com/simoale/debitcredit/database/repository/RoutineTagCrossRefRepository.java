package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.RoutineTagCrossRefDAO;
import com.simoale.debitcredit.model.RoutineTagCrossRef;

import java.util.List;

public class RoutineTagCrossRefRepository {
    private RoutineTagCrossRefDAO routineTagDao;
    private LiveData<List<RoutineTagCrossRef>> routineTagList;

    public RoutineTagCrossRefRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        this.routineTagDao = db.RoutineTagCrossRefDAO();
        this.routineTagList = routineTagDao.getRoutineTag();
    }

    public LiveData<List<RoutineTagCrossRef>> getRoutineTag() {
        return routineTagList;
    }

    public void addRoutineTag(final RoutineTagCrossRef routineTag) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> this.routineTagDao.addRoutineTag(routineTag));
    }

    public void addRoutineTags(final RoutineTagCrossRef[] routineTags) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> this.routineTagDao.addRoutineTags(routineTags));
    }

    public LiveData<List<String>> getTagsOfRoutine(int routineId) {
        return this.routineTagDao.getTagsOfRoutine(routineId);
    }
}
