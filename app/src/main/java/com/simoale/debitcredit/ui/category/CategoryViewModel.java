package com.simoale.debitcredit.ui.category;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.CategoryRepository;
import com.simoale.debitcredit.database.repository.WalletRepository;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository repository;

    // The wallet list
    private LiveData<List<Category>> categoryList;
    // The wallet currently selected
    private final MutableLiveData<Category> categorySelected = new MutableLiveData<>();

    public CategoryViewModel(Application application) {
        super(application);
        repository = new CategoryRepository(application);
        categoryList = repository.getCategoryList();
    }

    public void addCategory(Category category){
        repository.addCategory(category);
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public Category getCategory(int position){
        return categoryList.getValue() == null ? null : categoryList.getValue().get(position);
    }

    public void select(Category category) {
        categorySelected.setValue(category);
    }

    public LiveData<Category> getSelected() {
        return categorySelected;
    }
}






