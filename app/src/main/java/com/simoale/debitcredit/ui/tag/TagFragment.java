package com.simoale.debitcredit.ui.tag;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;

public class TagFragment extends Fragment {
    private static final String LOG = "Tag-Fragment_SIMOALE";
    private View view;

    private TagViewModel tagViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);
        this.view = root;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            tagViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TagViewModel.class);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

}