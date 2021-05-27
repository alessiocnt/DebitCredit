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
import com.simoale.debitcredit.recyclerView.PayeeCardAdapter;
import com.simoale.debitcredit.ui.payee.PayeeViewModel;

public class EditPayeesFragment extends Fragment {
    private static final String TAG = "SimoAle-EditPayeesFragment";

    private Activity activity;
    private PayeeViewModel payeeViewModel;
    private RecyclerView recyclerView;
    private PayeeCardAdapter payeeCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.payeeViewModel = new ViewModelProvider(this).get(PayeeViewModel.class);
        return inflater.inflate(R.layout.edit_payees, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {
            setRecyclerView(activity);
            this.payeeViewModel.getPayeeList().observe((LifecycleOwner) activity, categories -> payeeCardAdapter.setData(categories));
        } else {
            Log.e(TAG, "Activity is null");
        }
    }

    private void setRecyclerView(Activity activity) {
        recyclerView = getView().findViewById(R.id.payees_recyclerView);
        recyclerView.setHasFixedSize(true);
        payeeCardAdapter = new PayeeCardAdapter(activity);
        recyclerView.setAdapter(payeeCardAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.payeeViewModel.getPayeeList().removeObservers((LifecycleOwner) this.activity);
    }
}
