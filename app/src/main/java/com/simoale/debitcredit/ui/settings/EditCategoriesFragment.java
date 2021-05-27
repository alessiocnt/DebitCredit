package com.simoale.debitcredit.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.CategoryCardAdapter;
import com.simoale.debitcredit.ui.category.CategoryViewModel;

public class EditCategoriesFragment extends Fragment {
    private static final String TAG = "SimoAle-EditCategoriesFragment";

    private Activity activity;
    private CategoryViewModel categoryViewModel;
    private RecyclerView recyclerView;
    private CategoryCardAdapter categoryCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        return inflater.inflate(R.layout.edit_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {
            setRecyclerView(activity);
            this.categoryViewModel.getCategoryList().observe((LifecycleOwner) activity, categories -> categoryCardAdapter.setData(categories));
        } else {
            Log.e(TAG, "Activity is null");
        }
    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.categories_recyclerView);
        recyclerView.setHasFixedSize(true);
        categoryCardAdapter = new CategoryCardAdapter(activity);
        recyclerView.setAdapter(categoryCardAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.categoryViewModel.getCategoryList().removeObservers((LifecycleOwner) this.activity);
    }
}
