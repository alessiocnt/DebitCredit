package com.simoale.debitcredit.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.simoale.debitcredit.R;

public class RoutineFragment extends Fragment {

    private RoutineViewModel routineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routineViewModel =
                new ViewModelProvider(this).get(RoutineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_budget, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        routineViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}