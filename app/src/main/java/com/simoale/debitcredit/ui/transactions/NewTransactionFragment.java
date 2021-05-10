package com.simoale.debitcredit.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.simoale.debitcredit.R;

public class NewTransactionFragment extends Fragment {

    public NewTransactionFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.new_transaction, container, false);

        return root;
    }
}