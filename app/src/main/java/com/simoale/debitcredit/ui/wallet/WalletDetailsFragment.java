package com.simoale.debitcredit.ui.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.TransactionCardAdapter;
import com.simoale.debitcredit.ui.transactions.TransactionViewModel;
import com.simoale.debitcredit.utils.DatePicker;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class WalletDetailsFragment extends Fragment implements OnItemListener {
    private static final String LOG = "Wallet-Details-Fragment_SIMOALE";
    private View view;

    private TransactionCardAdapter transactionAdapter;
    private WalletViewModel walletViewModel;
    private TransactionViewModel transactionViewModel;
    private RecyclerView recyclerView;

    private Button applyFiltersBtn;
    private TextInputLayout searchText;
    private ChipGroup categoryChipGroup;
    private ChipGroup tagChipGroup;
    private ImageButton calendarFromBtn;
    private TextView calendarFromTextView;
    private ImageButton calendarToBtn;
    private TextView calendarToTextView;

    private String fromDate;
    private String toDate;

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

            this.fromDate = null;
            this.toDate = null;

            this.setupUi();

            walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            this.wallet = walletViewModel.getSelected().getValue();
            walletIcon.getDrawable().setTint(Integer.parseInt(this.wallet.getImage()));
            walletName.setText(this.wallet.getName());
            walletBalance.setText(this.wallet.getBalance() + "€");
            walletDescription.setText(this.wallet.getDescription());
            transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            // TODO selezionare solo la lista del wallet interessato che si trova in walletViewModel.getSelected()
            transactionViewModel.getTransactionList(this.wallet.getId(), this.wallet.getId(), null, null, null, null, null).observe((LifecycleOwner) activity, transactions -> {
                transactionAdapter.setData(transactions);
                transactionViewModel.getTransactionList().removeObservers((LifecycleOwner) activity);
            });
            AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));
            AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
            AtomicInteger day = new AtomicInteger(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            this.calendarFromBtn.setOnClickListener(v -> {
                DatePicker datePicker = new DatePicker(year.get(), month.get(), day.get());
                datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
                datePicker.getDataReady().observe(getActivity(), value -> {
                    if (value) {
                        year.set(datePicker.getYear());
                        month.set(datePicker.getMonth());
                        day.set(datePicker.getDay());
                        this.fromDate = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDay());
                        this.calendarFromTextView.setText(String.format("From: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                    }
                });
            });
            this.calendarToBtn.setOnClickListener(v -> {
                DatePicker datePicker = new DatePicker(year.get(), month.get(), day.get());
                datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
                datePicker.getDataReady().observe(getActivity(), value -> {
                    if (value) {
                        year.set(datePicker.getYear());
                        month.set(datePicker.getMonth());
                        day.set(datePicker.getDay());
                        this.toDate = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDay());
                        this.calendarToTextView.setText(String.format("To: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                    }
                });
            });
            this.applyFiltersBtn.setOnClickListener(v -> {
                this.computeFilters();
            });
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setupUi() {
        this.applyFiltersBtn = view.findViewById(R.id.wallet_details_search_button);
        this.calendarFromBtn = view.findViewById(R.id.transaction_search_start_date_calendar);
        this.calendarFromTextView = view.findViewById(R.id.transaction_search_start_date);
        this.calendarToBtn = view.findViewById(R.id.transaction_search_end_date_calendar);
        this.calendarToTextView = view.findViewById(R.id.transaction_search_end_date);
        this.searchText = view.findViewById(R.id.wallet_transaction_search);
        this.categoryChipGroup = view.findViewById(R.id.transaction_search_category_chip_group);
        this.tagChipGroup = view.findViewById(R.id.transaction_search_tag_chip_group);
    }

    private void computeFilters() {
        Toast.makeText(getContext(), "ah nice", Toast.LENGTH_LONG).show();
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
