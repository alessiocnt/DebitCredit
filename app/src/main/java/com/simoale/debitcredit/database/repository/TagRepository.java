package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.TagDAO;
import com.simoale.debitcredit.model.Tag;

import java.util.List;

public class TagRepository {
    private TagDAO tagDAO;
    private LiveData<List<Tag>> tagList;

    public TagRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        this.tagDAO = db.TagDAO();
        this.tagList = tagDAO.getTags();
    }

    public LiveData<List<Tag>> getTagList() {
        return tagList;
    }

    public void addTag(final Tag tag) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> this.tagDAO.addTag(tag));
    }
}
