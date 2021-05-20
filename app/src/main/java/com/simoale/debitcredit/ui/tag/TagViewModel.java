package com.simoale.debitcredit.ui.tag;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.CategoryRepository;
import com.simoale.debitcredit.database.repository.TagRepository;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Tag;

import java.util.List;

public class TagViewModel extends AndroidViewModel {

    private TagRepository repository;

    private LiveData<List<Tag>> tagList;
    private final MutableLiveData<Tag> tagSelected = new MutableLiveData<>();

    public TagViewModel(Application application) {
        super(application);
        repository = new TagRepository(application);
        tagList = repository.getTagList();
    }

    public void addTag(Tag tag){
        repository.addTag(tag);
    }

    public LiveData<List<Tag>> getTagList() {
        return tagList;
    }

    public Tag getTag(int position){
        return tagList.getValue() == null ? null : tagList.getValue().get(position);
    }

    public void select(Tag tag) {
        tagSelected.setValue(tag);
    }

    public LiveData<Tag> getSelected() {
        return tagSelected;
    }
}






