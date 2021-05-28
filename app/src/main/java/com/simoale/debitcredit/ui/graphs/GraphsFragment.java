package com.simoale.debitcredit.ui.graphs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.ui.tag.TagViewModel;
import com.simoale.debitcredit.ui.transactions.TransactionViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;
import com.simoale.debitcredit.utils.DatePicker;
import com.simoale.debitcredit.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphsFragment extends Fragment {
    private static final String LOG = "Graphs-Fragment_SIMOALE";
    private Activity activity;
    private View view;
    // private GraphsViewModel graphsViewModel;
    private TransactionViewModel transactionViewModel;
    private WalletViewModel walletViewModel;
    private CategoryViewModel categoryViewModel;

    private List<String> categorySelected;
    private List<String> walletSelected;
    private String dateSelectedFrom;
    private String dateSelectedTo;

    private Button applyBtn;
    private TextView dateDisplayFrom;
    private TextView dateDisplayTo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // graphsViewModel = new ViewModelProvider(this).get(GraphsViewModel.class);
        this.view = inflater.inflate(R.layout.fragment_graphs, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();
        if (activity != null) {

            this.transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryViewModel.class);

            setupUi();
            setupChips();
            setupDatePicker();

            this.applyBtn.setOnClickListener(v -> {
                if (Utilities.checkDataValid(dateSelectedFrom, dateSelectedTo)) {
                    this.computeFilters();
                } else {
                    Toast.makeText(activity.getBaseContext(), "Select dates to analise a period", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    public void setupUi() {
        this.dateDisplayFrom = activity.findViewById(R.id.graphs_date_from_date);
        this.dateDisplayTo = activity.findViewById(R.id.graphs_date_to_date);
        this.applyBtn = getView().findViewById(R.id.graphs_search_button);
    }

    private void setupChips() {
        // Category chip group
        ChipGroup categoryChipGroup = getView().findViewById(R.id.graphs_category_chip_group);
        setupCategoryChips(categoryChipGroup);
        // Wallet chip group
        ChipGroup walletChipGroup = getView().findViewById(R.id.graphs_wallet_chip_group);
        setupWalletChips(walletChipGroup);
    }

    private void setupCategoryChips(ChipGroup categoryChipGroup) {
        this.categorySelected = new ArrayList<>();
        categoryViewModel.getCategoryList().observe((LifecycleOwner) activity, category -> {
            for (Category cat : category) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, categoryChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(cat.getName());
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        categorySelected.add(chip.getText().toString());
                    } else {
                        categorySelected.remove(chip.getText().toString());
                    }
                });
                categoryChipGroup.addView(chip);
            }
            categoryViewModel.getCategoryList().removeObservers((LifecycleOwner) activity);
        });
    }

    private void setupWalletChips(ChipGroup walletChipGroup) {
        this.walletSelected = new ArrayList<>();
        walletViewModel.getWalletList().observe((LifecycleOwner) activity, wallets -> {
            for (Wallet wallet : wallets) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, walletChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(wallet.getName());
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        walletSelected.add(chip.getText().toString());
                    } else {
                        walletSelected.remove(chip.getText().toString());
                    }
                });
                walletChipGroup.addView(chip);
            }
            walletViewModel.getWalletList().removeObservers((LifecycleOwner) activity);
        });
    }

    private void setupDatePicker() {
        setupDatePickerFrom();
        setupDatePickerTo();
    }

    private void setupDatePickerFrom() {
        ImageButton calendar = getView().findViewById(R.id.graphs_date_from_calendar);
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
                    dateSelectedFrom = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDay());
                    this.dateDisplayFrom.setText(String.format("From: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                }
            });
        });
    }

    private void setupDatePickerTo() {
        ImageButton calendar = getView().findViewById(R.id.graphs_date_to_calendar);
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
                    dateSelectedTo = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDay());
                    this.dateDisplayTo.setText(String.format("To: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                }
            });
        });
    }

    private void computeFilters() {
        transactionViewModel.getTransactionList(dateSelectedFrom, dateSelectedTo).observe((LifecycleOwner) getActivity(), transactions -> {
            Log.e("trans", transactions.size() + "");
            Log.e("from", dateSelectedFrom );
            Log.e("to", dateSelectedTo );
            generateCharts(transactions);
            transactionViewModel.getTransactionList(dateSelectedFrom, dateSelectedTo).removeObservers((LifecycleOwner) getActivity());
        });

        /*transactionViewModel.getTransactionList().observe((LifecycleOwner) getActivity(), transactions -> {
            generateCharts(transactions);
            transactionViewModel.getTransactionList().removeObservers((LifecycleOwner) getActivity());
        });*/
    }

    private void generateCharts(List<Transaction> transactions) {
        AnyChartView lineChartView = view.findViewById(R.id.line_chart);
        lineChartView.setProgressBar(view.findViewById(R.id.line_chart_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(lineChartView);
        // Prepare data
        HashSet<String> categorySet = new HashSet<>();
        // Retrive Wallet ID's from names (selected)
        List<Integer> walletIDList = new ArrayList<>();
        walletViewModel.getWalletListFromNames(walletSelected).observe((LifecycleOwner) activity, o -> o.forEach(w -> walletIDList.add(w.getId())));
        // Filter the transactions due to the selections of wallets and categories
        transactions.forEach(t -> {
            if (categorySelected.contains(t.getCategoryName())) {
                categorySet.add(t.getCategoryName());
            }
        });
        // Store all the lists
        Map<String, List<DataEntry>> lineData = new HashMap<>();
        categorySet.forEach(c -> {
            List<DataEntry> categoryDataList = new ArrayList<>();
            transactions.forEach(t -> {
                if (c.equals(t.getCategoryName()) && walletIDList.contains(t.getWalletIdTo())) {
                    categoryDataList.add(new ValueDataEntry(t.getDate(), t.getAmount()));
                }
            });
            lineData.put(c, categoryDataList);
        });
        Chart lineChart = new LineChart(lineChartView, lineData, "Category trendline", "Time", "Amount(€)");
        lineChart.instantiateChart();

        /*******************/

        AnyChartView columnChartView = view.findViewById(R.id.candle_chart);
        columnChartView.setProgressBar(view.findViewById(R.id.candle_chart_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(columnChartView);

        // Store all the columns
        List<DataEntry> columnData = new ArrayList<>();
        categorySet.forEach(c -> {
            AtomicInteger amount = new AtomicInteger(0);
            transactions.forEach(t -> {
                if (c.equals(t.getCategoryName()) && walletIDList.contains(t.getWalletIdTo())) {
                    amount.set(amount.intValue() - (int) t.getAmount());
                }
            });
            columnData.add(new ValueDataEntry(c, amount.intValue()));
        });

        Chart columnChart = new ColumnChart(columnChartView, columnData, "Expenses overview", "Categories", "Expenses");
        columnChart.instantiateChart();

    }

}