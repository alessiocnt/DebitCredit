package com.simoale.debitcredit.ui.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.simoale.debitcredit.R;

public class WalletDetailsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wallet_details, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO Remove button
        Button tmpBtn = view.findViewById(R.id.show_transaction_details);
        tmpBtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.action_wallet_details_fragment_to_transactionDetailsFragment);
        });
    }
}
