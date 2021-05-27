package com.simoale.debitcredit.ui.budget;

import android.app.Activity;
import android.os.Bundle;
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
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.utils.DatePicker;
import com.simoale.debitcredit.utils.Utilities;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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
            prepareAutoCompleteTextViews();

            this.saveBtn.setOnClickListener(v -> {
                // Retrive data
                String name = nameEditText.getEditText().getText().toString();
                String limit = limitEditText.getEditText().getText().toString().replace(',', '.');
                String number = numberTextView.getText().toString();
                String interval = intervalTextView.getText().toString().replace("(s)", "");

                // Insert data
                if (Utilities.checkDataValid(name, limit, number, interval, categorySelected, dateSelected)) {
                    // Make the transaction
                    budgetViewModel.addBudget(new Budget(name,
                            categorySelected, Float.parseFloat(limit), dateSelected, dateSelected, null, Integer.parseInt(number), interval, Float.parseFloat(limit)));
                    Navigation.findNavController(v).navigate(R.id.action_newBudgetFragment_to_nav_budget);
                } else {
                    Toast.makeText(activity.getBaseContext(), "Every field must be filled", Toast.LENGTH_LONG).show();
                }
            });
            this.cancelBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_newBudgetFragment_to_nav_budget);
            });
        }
    }

    private void setupUi() {
        this.nameEditText = activity.findViewById(R.id.budget_name_TextInput);
        this.limitEditText = activity.findViewById(R.id.budget_limit_TextInput);
        this.dateDisplay = activity.findViewById(R.id.budget_start_date);
        this.numberTextView = activity.findViewById(R.id.budget_repeat_number_autocomplete);
        this.intervalTextView = activity.findViewById(R.id.budget_repeat_interval_autocomplete);
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

    private void prepareAutoCompleteTextViews() {
        String[] times = IntStream.range(1, 31).mapToObj(String::valueOf).toArray(String[]::new);
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, times);
        this.numberTextView.setAdapter(timesAdapter);

        String[] interval = new String[]{"Day(s)", "Week(s)", "Month(s)", "Year(s)"};
        ArrayAdapter<String> intervalAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, interval);
        this.intervalTextView.setAdapter(intervalAdapter);
    }
}