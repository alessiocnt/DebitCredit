package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.TagDAO;
import com.simoale.debitcredit.model.Tag;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public void editTag(Tag oldTag, Tag newTag) {
        DatabaseInstance.databaseWriteExecutor.execute(() -> tagDAO.editTag(oldTag.getName(), newTag.getName()));
    }

    public boolean deleteTag(Tag tag) {
        AtomicBoolean ret = new AtomicBoolean(true);
        DatabaseInstance.databaseWriteExecutor.execute(() -> {
            try {
                tagDAO.deleteTag(tag);
            } catch (Exception e) {
                ret.set(false);
            }
        });
        try {
            DatabaseInstance.databaseWriteExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret.get();
    }
}
