package com.simoale.debitcredit.ui.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.TransactionCardAdapter;
import com.simoale.debitcredit.ui.transactions.TransactionViewModel;

import java.util.List;

public class WalletDetailsFragment extends Fragment implements OnItemListener {
    private static final String LOG = "Wallet-Details-Fragment_SIMOALE";
    private View view;

    private TransactionCardAdapter transactionAdapter;
    private WalletViewModel walletViewModel;
    private TransactionViewModel transactionViewModel;
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wallet_details, container, false);
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
            transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            // TODO selezionare solo la lista del wallet interessato che si trova in walletViewModel.getSelected()
            transactionViewModel.getTransactionList().observe((LifecycleOwner) activity, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(List<Transaction> transactions) {
                    transactionAdapter.setData(transactions);
                }
            });
        } else {
            Log.e(LOG, "Activity is null");
        }

    }

    // // Set up the RecyclerView
    private void setRecyclerView(final Activity activity) {
        recyclerView = getView().findViewById(R.id.wallet_details_recycler_view);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        transactionAdapter = new TransactionCardAdapter(activity, listener);
        recyclerView.setAdapter(transactionAdapter);
    }

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            transactionViewModel.select(transactionAdapter.getTransaction(position));
            Navigation.findNavController(view).navigate(R.id.action_wallet_details_fragment_to_transactionDetailsFragment);
        }
    }
}
