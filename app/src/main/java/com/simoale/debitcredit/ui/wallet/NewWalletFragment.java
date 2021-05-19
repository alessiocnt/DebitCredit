package com.simoale.debitcredit.ui.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Wallet;

public class NewWalletFragment extends Fragment {

    private Button saveBtn;
    private WalletViewModel walletViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_wallet, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            this.saveBtn = getView().findViewById(R.id.save_btn);
            this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            this.saveBtn.setOnClickListener(v -> {
                TextInputLayout walletNameEditText = activity.findViewById(R.id.wallet_name_TextInput);
                String walletName = walletNameEditText.getEditText().getText().toString();
                TextInputLayout walletAmountEditText = activity.findViewById(R.id.wallet_initial_value);
                String walletAmount = walletAmountEditText.getEditText().getText().toString();
                this.walletViewModel.addWallet(new Wallet(walletName, "nice", Integer.parseInt(walletAmount), "null"));
            });
        }
    }
}