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
import com.simoale.debitcredit.recyclerView.TagCardAdapter;
import com.simoale.debitcredit.ui.tag.TagViewModel;

public class EditTagsFragment extends Fragment {
    private static final String TAG = "SimoAle-EditTagsFragment";

    private Activity activity;
    private TagViewModel tagViewModel;
    private RecyclerView recyclerView;
    private TagCardAdapter tagCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        return inflater.inflate(R.layout.edit_tags, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {
            setRecyclerView(activity);
            this.tagViewModel.getTagList().observe((LifecycleOwner) activity, tags -> tagCardAdapter.setData(tags));
        } else {
            Log.e(TAG, "Activity is null");
        }
    }

    private void setRecyclerView(Activity activity) {
        recyclerView = getView().findViewById(R.id.tags_recyclerView);
        recyclerView.setHasFixedSize(true);
        tagCardAdapter = new TagCardAdapter(activity);
        recyclerView.setAdapter(tagCardAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.tagViewModel.getTagList().removeObservers((LifecycleOwner) activity);
    }
}
