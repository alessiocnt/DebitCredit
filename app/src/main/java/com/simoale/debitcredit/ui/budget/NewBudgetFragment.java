package com.simoale.debitcredit.ui.budget;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
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
import com.simoale.debitcredit.model.TransactionTagCrossRef;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.ui.payee.PayeeViewModel;
import com.simoale.debitcredit.ui.tag.TagViewModel;
import com.simoale.debitcredit.ui.transactions.TransactionTagViewModel;
import com.simoale.debitcredit.ui.transactions.TransactionViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;
import com.simoale.debitcredit.utils.DatePicker;
import com.simoale.debitcredit.utils.LocationUtils;
import com.simoale.debitcredit.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NewBudgetFragment extends Fragment {

    private Activity activity;
    private BudgetViewModel budgetViewModel;
    private CategoryViewModel categoryViewModel;

    private TextInputLayout nameEditText;
    private TextInputLayout limitEditText;
    String categorySelected;
    private TextView dateDisplay;
    private String dateSelected;
    private AutoCompleteTextView numberTextView;
    private AutoCompleteTextView intervalTextView;
    private Button saveBtn;
    private Button cancelBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_budget, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (activity != null) {
            // Getting the views
            setupUi();
            // Creation of needed viewMoldel to retrive data
            this.budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(BudgetViewModel.class);
            this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryViewModel.class);

            setupDatePicker();
            setupChips();

            this.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*try {
                        // Retrive data
                        Integer amount = transactionType.getType() * Math.abs(Integer.parseInt(amountEditText.getEditText().getText().toString()));
                        String description = descriptionEditText.getEditText().getText().toString();
                        String location = locationText.getText().toString();
                        String note = noteEditText.getEditText().getText().toString();
                        Bitmap bitmap = transactionViewModel.getBitmap().getValue();
                        String imageUriString;
                        if (bitmap != null) {
                            //method to save the image in the gallery of the device
                            imageUriString = String.valueOf(saveImage(bitmap, activity));
                            //Toast.makeText(activity,"Image Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            imageUriString = "ic_launcher_foreground";
                        }
                        // Insert data
                        Wallet currentWallet = walletViewModel.getWalletFromName(walletSelected).getValue();
                        if (Utilities.checkDataValid(amount.toString(), categorySelected, dateSelected, walletSelected)) {
                            // Make the transaction
                            int lastTransactionID = (int) transactionViewModel.addTransaction(new Transaction(amount,
                                    description, categorySelected, payeeSelected, dateSelected, currentWallet.getId(), currentWallet.getId(), location, note, imageUriString));
                            // Update Wallet balance
                            walletViewModel.updateBalance(currentWallet.getId(), amount);
                            // Add transaction tag's

                            List<TransactionTagCrossRef> transactionTagList = new ArrayList<>();
                            tagSelected.forEach(tag -> transactionTagList.add(new TransactionTagCrossRef(lastTransactionID, tag)));
                            TransactionTagCrossRef transactionTagArray[] = new TransactionTagCrossRef[transactionTagList.size()];
                            for (int i = 0; i < transactionTagList.size(); i++) {
                                transactionTagArray[i] = transactionTagList.get(i);
                            }
                            transactionTagViewModel.addTransactionTags(transactionTagArray);
                            Navigation.findNavController(v).navigate(R.id.action_newTransactionTabFragment_to_nav_home);
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Every field must be filled", Toast.LENGTH_LONG).show();
                        }
                        transactionViewModel.setImageBitmpap(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            });
            this.cancelBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_newTransactionTabFragment_to_nav_home);
            });
        }
    }

    private void setupUi() {
        this.nameEditText = activity.findViewById(R.id.budget_name);
        this.limitEditText = activity.findViewById(R.id.budget_limit_TextInput);
        this.dateDisplay = activity.findViewById(R.id.budget_start_date);
        this.numberTextView = activity.findViewById(R.id.budget_repeat_number);
        this.numberTextView = activity.findViewById(R.id.budget_repeat_interval);
        this.saveBtn = activity.findViewById(R.id.budget_save_button);
        this.cancelBtn = activity.findViewById(R.id.budget_cancel_button);
    }

    private void setupDatePicker() {
        ImageButton calendar = getView().findViewById(R.id.budget_calendar);
        AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));
        AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
        AtomicInteger day = new AtomicInteger(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        calendar.setOnClickListener(v -> {
            DatePicker datePicker = new DatePicker(year.get(), month.get(), day.get());
            datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
            datePicker.getDataReady().observe(getActivity(), value -> {
                if (value) {
                    year.set(datePicker.getYear());
                    month.set(datePicker.getMonth());
                    day.set(datePicker.getDay());
                    this.dateSelected = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth(), datePicker.getDay());
                    this.dateDisplay.setText(String.format("Start date: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                }
            });
        });
    }

    private void setupChips() {
        // Category chip group
        ChipGroup categoryChipGroup = getView().findViewById(R.id.budget_category_chip_group);
        setupCategoryChips(categoryChipGroup);
    }

    private void setupCategoryChips(ChipGroup categoryChipGroup) {
        categoryViewModel.getCategoryList().observe((LifecycleOwner) activity, category -> {
            categoryChipGroup.removeAllViews();
            for (Category cat : category) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, categoryChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(cat.getName());
                categoryChipGroup.addView(chip);
            }
            categoryViewModel.getCategoryList().removeObservers((LifecycleOwner) activity);
        });
        categoryChipGroup.setOnCheckedChangeListener((chipGroup, i) -> {
            Chip chip = chipGroup.findViewById(i);
            // Set the chosen category
            categorySelected = chip.getText().toString();
        });
        ImageButton add = getView().findViewById(R.id.budget_add_category);
        add.setOnClickListener(v -> {
            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            dialogBuilder.setMessage("Remember that an item must be unique!")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            categoryViewModel.addCategory(new Category(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Insert a category name to create a new one", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }
}