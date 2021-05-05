package com.simoale.debitcredit.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.simoale.debitcredit.R;

public class BudgetFragment extends Fragment {

    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_budget, container, false);
        return this.view;
    }
}
