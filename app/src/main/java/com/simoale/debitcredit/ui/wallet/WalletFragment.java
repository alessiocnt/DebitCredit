package com.simoale.debitcredit.ui.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.WalletCardAdapter;
import com.simoale.debitcredit.ui.home.HomeViewModel;

import java.util.List;

public class WalletFragment extends Fragment implements OnItemListener {
    private static final String LOG = "Wallet-Fragment_SIMOALE";
    private View view;

    private WalletCardAdapter walletAdapter;
    private WalletViewModel walletViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walletViewModel =
                new ViewModelProvider(this).get(WalletViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);
        this.view = root;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            setRecyclerView(activity);

            walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            //when the list of the wallets changed, the adapter gets the new list.
            walletViewModel.getWalletList().observe((LifecycleOwner) activity, new Observer<List<Wallet>>() {
                @Override
                public void onChanged(List<Wallet> wallets) {
                    walletAdapter.setData(wallets);
                }
            });

            FloatingActionButton fab = view.findViewById(R.id.fab_add);
            fab.setOnClickListener((v) -> Navigation.findNavController(v).navigate(R.id.action_nav_wallet_to_new_wallet));
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.wallet_fragment_recycler_view);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        walletAdapter = new WalletCardAdapter(activity, listener);
        recyclerView.setAdapter(walletAdapter);
    }

    @Override
    public void onItemClick(int position) {
        // Handle the action on a click on a recyclerView element
        // Saves the current tapped item in the viewModel
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            walletViewModel.select(walletAdapter.getWallet(position));
            Navigation.findNavController(view).navigate(R.id.action_walletFragment_to_walletDetailsFragment);
        }
    }
}