package com.simoale.debitcredit.ui.payee;

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

public class PayeeFragment extends Fragment {
    private static final String LOG = "Payee-Fragment_SIMOALE";
    private View view;

    private PayeeViewModel payeeViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        payeeViewModel =
                new ViewModelProvider(this).get(PayeeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);
        this.view = root;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {

            payeeViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PayeeViewModel.class);

        } else {
            Log.e(LOG, "Activity is null");
        }
    }

}