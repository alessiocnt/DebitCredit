package com.simoale.debitcredit.ui.transactions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Payee;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.ui.payee.PayeeViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;

import java.util.Calendar;
import java.util.List;

public class NewTransactionFragment extends Fragment {

    private Activity activity;
    private Button saveBtn;
    private Button cancelBtn;

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private PayeeViewModel payeeViewModel;

    public NewTransactionFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_transaction, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (activity != null) {
            this.saveBtn = getView().findViewById(R.id.transaction_save_button);
            this.cancelBtn = getView().findViewById(R.id.transaction_cancel_button);
            this.transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryViewModel.class);
            this.payeeViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PayeeViewModel.class);

            setupDatePicker();
            setupChips();
            
            
            
            /*this.saveBtn.setOnClickListener(v -> {
                retriveData();

                TextInputLayout walletNameEditText = activity.findViewById(R.id.wallet_name_TextInput);
                String walletName = walletNameEditText.getEditText().getText().toString();
                TextInputLayout walletAmountEditText = activity.findViewById(R.id.wallet_initial_value);
                String walletAmount = walletAmountEditText.getEditText().getText().toString();
                this.walletViewModel.addWallet(new Wallet(walletName, "nice", Integer.parseInt(walletAmount), "null"));
            });*/
            
        }
    }

    private void setupChips() {
        // Category chip group
        ChipGroup categoryChipGroup = getView().findViewById(R.id.transaction_category_chip_group);
        setupCategoryChips(categoryChipGroup);
        // Payee chip group
        ChipGroup payeeChipGroup = getView().findViewById(R.id.transaction_payee_chip_group);
        setupPayeeChips(payeeChipGroup);
    }

    private void setupPayeeChips(ChipGroup payeeChipGroup) {
        payeeViewModel.getPayeeList().observe((LifecycleOwner) activity, new Observer<List<Payee>>() {
            @Override
            public void onChanged(List<Payee> payee) {
                for(Payee p : payee) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, payeeChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(p.getName());
                    payeeChipGroup.addView(chip);
                }
            }
        });

        payeeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen payee
                TextInputLayout payeeEditText = activity.findViewById(R.id.transaction_payee_TextInput);
                payeeEditText.setHint(chip.getText());
            }
        });
    }

    private void setupCategoryChips(ChipGroup categoryChipGroup) {
        categoryViewModel.getCategoryList().observe((LifecycleOwner) activity, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> category) {
                for(Category cat : category) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, categoryChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(cat.getName());
                    categoryChipGroup.addView(chip);
                }
            }
        });

        categoryChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen category
                TextInputLayout categoryEditText = activity.findViewById(R.id.transaction_category_TextInput);
                categoryEditText.setHint(chip.getText());
            }
        });
    }

    private void setupDatePicker() {
        ImageButton calendar = getView().findViewById(R.id.calendar);
        calendar.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
        });
    }

    private Transaction retriveData() {
        TextInputLayout amountEditText = activity.findViewById(R.id.transaction_amount_InputEditText);
        String amount = amountEditText.getEditText().getText().toString();

        TextInputLayout descriptionEditText = activity.findViewById(R.id.transaction_description_InputEditText);
        String description = descriptionEditText.getEditText().getText().toString();

        TextInputLayout categoryEditText = activity.findViewById(R.id.transaction_category_InputEditText);
        String category = categoryEditText.getEditText().getText().toString();

        TextInputLayout payeeEditText = activity.findViewById(R.id.transaction_payee_InputEditText);
        String payee = payeeEditText.getEditText().getText().toString();

        return null;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView dateDisplay = getActivity().findViewById(R.id.date_display);
            dateDisplay.setText("Date: " + day + "/" + month + "/" + year);
        }
    }
}