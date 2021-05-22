package com.simoale.debitcredit.ui.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.TransactionCardAdapter;
import com.simoale.debitcredit.ui.transactions.TransactionViewModel;

public class WalletDetailsFragment extends Fragment implements OnItemListener {
    private static final String LOG = "Wallet-Details-Fragment_SIMOALE";
    private View view;

    private TransactionCardAdapter transactionAdapter;
    private WalletViewModel walletViewModel;
    private TransactionViewModel transactionViewModel;
    private RecyclerView recyclerView;

    private Wallet wallet;


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
            ImageView walletIcon = view.findViewById(R.id.wallet_details_image);
            TextView walletName = view.findViewById(R.id.wallet_details_name);
            TextView walletBalance = view.findViewById(R.id.wallet_details_balance);
            TextView walletDescription = view.findViewById(R.id.wallet_details_description);
            walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            this.wallet = walletViewModel.getSelected().getValue();
            walletIcon.getDrawable().setTint(Integer.parseInt(this.wallet.getImage()));
            walletName.setText(this.wallet.getName());
            walletBalance.setText(this.wallet.getBalance() + "€");
            walletDescription.setText(this.wallet.getDescription());
            transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            // TODO selezionare solo la lista del wallet interessato che si trova in walletViewModel.getSelected()
            transactionViewModel.getTransactionList(this.wallet.getId(), this.wallet.getId(), null, null, null, null).observe((LifecycleOwner) activity, transactions -> {
                transactionAdapter.setData(transactions);
                transactionViewModel.getTransactionList().removeObservers((LifecycleOwner) activity);
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
