package com.simoale.debitcredit.ui.routine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.simoale.debitcredit.model.Routine;
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.ui.payee.PayeeViewModel;
import com.simoale.debitcredit.ui.tag.TagViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;
import com.simoale.debitcredit.utils.DatePicker;
import com.simoale.debitcredit.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class NewRoutineFragment extends Fragment {

    private final String TAG = "SimoAle-NewRoutineFragment";
    private Activity activity;

    private RoutineViewModel routineViewModel;
    private WalletViewModel walletViewModel;
    private CategoryViewModel categoryViewModel;
    private PayeeViewModel payeeViewModel;

    private TextInputLayout routineNameTextInput;
    private TextInputLayout routineImportTextInput;
    private ChipGroup walletChipGroup;
    private ChipGroup categoryChipGroup;
    private ChipGroup payeeChipGroup;
    private TextView dateStartTextView;
    private ImageButton calendarButton;
    private AutoCompleteTextView timesTextView;
    private AutoCompleteTextView intervalTextView;
    private ImageButton addCategoryButton;
    private ImageButton addPayeeButton;
    private Button saveButton;
    private Button cancelButton;

    private String selectedWallet;
    private String selectedCategory;
    private String selectedPayee;
    private String dateSelected;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_routine, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();
        if (activity != null) {
            this.routineViewModel = new ViewModelProvider((ViewModelStoreOwner) this.activity).get(RoutineViewModel.class);
            this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) this.activity).get(WalletViewModel.class);
            this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) this.activity).get(CategoryViewModel.class);
            this.payeeViewModel = new ViewModelProvider((ViewModelStoreOwner) this.activity).get(PayeeViewModel.class);

            this.routineNameTextInput = view.findViewById(R.id.routine_name_TextInput);
            this.routineImportTextInput = view.findViewById(R.id.routine_import_TextInput);
            this.walletChipGroup = view.findViewById(R.id.new_routine_wallet_chip_group);
            this.categoryChipGroup = view.findViewById(R.id.new_routine_category_chip_group);
            this.payeeChipGroup = view.findViewById(R.id.new_routine_payee_chip_group);
            this.dateStartTextView = view.findViewById(R.id.routine_start_date);
            this.calendarButton = view.findViewById(R.id.new_routine_calendar);
            this.timesTextView = view.findViewById(R.id.routine_repeat_number_autocomplete);
            this.intervalTextView = view.findViewById(R.id.routine_repeat_interval_autocomplete);
            this.addCategoryButton = view.findViewById(R.id.new_routine_add_category);
            this.addPayeeButton = view.findViewById(R.id.new_routine_add_payee);
            this.saveButton = view.findViewById(R.id.new_routine_save_button);
            this.cancelButton = view.findViewById(R.id.new_routine_cancel_button);

            this.addButtonsListenerPrepare();
            this.prepareChipGroups();
            this.prepareCalendar();
            this.prepareAutoCompleteTextViews();


            this.saveButton.setOnClickListener(v -> this.handleSaveClick(v));
            this.cancelButton.setOnClickListener(v -> this.handleCancelClick(v));

        } else {
            Log.e(TAG, "activity is null");
        }
    }

    private void handleSaveClick(View view) {
        boolean dataOk = false;
        String routineName = this.routineNameTextInput.getEditText().getText().toString();
        String routineImport = this.routineImportTextInput.getEditText().getText().toString().replace(',', '.');
        Log.e("routineImport", routineImport);
        String times = this.timesTextView.getText().toString();
        String interval = this.intervalTextView.getText().toString().replace("(s)", "");

        dataOk = Utilities.checkDataValid(routineName, routineImport, this.selectedWallet, this.selectedCategory, this.selectedPayee, this.dateSelected, times, interval);

        if (dataOk) {
            this.routineViewModel.addRoutine(new Routine(routineName, Float.parseFloat(routineImport),
                    this.selectedPayee, this.walletViewModel.getWalletFromName(this.selectedWallet).getValue().getId(),
                    this.selectedCategory, this.dateSelected, this.dateSelected, null, Integer.parseInt(times), interval));
            Navigation.findNavController(view).navigate(R.id.action_newRoutineFragment_to_nav_routine);
        } else {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }

    }

    private void handleCancelClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_newRoutineFragment_to_nav_routine);
    }

    private void prepareAutoCompleteTextViews() {
        String[] times = IntStream.range(1, 31).mapToObj(String::valueOf).toArray(String[]::new);
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, times);
        this.timesTextView.setAdapter(timesAdapter);

        String[] interval = new String[]{"Day(s)", "Week(s)", "Month(s)", "Year(s)"};
        ArrayAdapter<String> intervalAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, interval);
        this.intervalTextView.setAdapter(intervalAdapter);
    }

    private void prepareCalendar() {
        AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));
        AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
        AtomicInteger day = new AtomicInteger(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        this.calendarButton.setOnClickListener(v -> {
            DatePicker datePicker = new DatePicker(year.get(), month.get(), day.get());
            datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
            datePicker.getDataReady().observe(getActivity(), value -> {
                if (value) {
                    year.set(datePicker.getYear());
                    month.set(datePicker.getMonth());
                    day.set(datePicker.getDay());
                    this.dateSelected = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDay());
                    this.dateStartTextView.setText(String.format("Start date: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                }
            });
        });
    }

    private void prepareChipGroups() {
        this.walletViewModel.getWalletList().observe((LifecycleOwner) this.activity, wallets -> {
            this.walletChipGroup.removeAllViews();
            for (Wallet wallet : wallets) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, payeeChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(wallet.getName());
                this.walletChipGroup.addView(chip);
            }

        });
        this.walletChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            this.selectedWallet = chip.getText().toString();
        });

        this.categoryViewModel.getCategoryList().observe((LifecycleOwner) this.activity, categories -> {
            this.categoryChipGroup.removeAllViews();
            for (Category category : categories) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, payeeChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(category.getName());
                this.categoryChipGroup.addView(chip);
            }

        });
        this.categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            this.selectedCategory = chip.getText().toString();
        });

        this.payeeViewModel.getPayeeList().observe((LifecycleOwner) this.activity, payees -> {
            this.payeeChipGroup.removeAllViews();
            for (Payee payee : payees) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, payeeChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(payee.getName());
                this.payeeChipGroup.addView(chip);
            }

        });
        this.payeeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            this.selectedPayee = chip.getText().toString();
        });


    }

    private void addButtonsListenerPrepare() {
        this.addCategoryButton.setOnClickListener(v -> {
            // Generate dialog for insertion
            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("New category name");
            dialogBuilder.setMessage("Add a new category\nRemember that an item must be unique!")
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

        this.addPayeeButton.setOnClickListener(v -> {
            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("New payee name");
            dialogBuilder.setMessage("Add a new payee\nRemember that an item must be unique!")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            payeeViewModel.addPayee(new Payee(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Insert a payee name to create a new one", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.walletViewModel.getWalletList().removeObservers((LifecycleOwner) this.activity);
        this.categoryViewModel.getCategoryList().removeObservers((LifecycleOwner) this.activity);
        this.payeeViewModel.getPayeeList().removeObservers((LifecycleOwner) this.activity);
    }
}