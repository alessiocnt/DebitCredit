package com.simoale.debitcredit.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.model.Category;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Application application;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void setApplication(Application application) {
        this.application = application;
        DatabaseInstance db = DatabaseInstance.getDatabase(this.application);
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.CategoryDAO().addCategory(new Category("a"));
            }
        });
        Log.e("New Category", db.CategoryDAO().getClass().getSimpleName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}