package com.simoale.debitcredit.ui.home;

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

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.WalletCardAdapter;
import com.simoale.debitcredit.ui.budget.BudgetViewModel;
import com.simoale.debitcredit.ui.graphs.Chart;
import com.simoale.debitcredit.ui.graphs.CircularGaugeChart;
import com.simoale.debitcredit.ui.routine.RoutineViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;

import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnItemListener {

    private static final String LOG = "Home-Fragment_SIMOALE";

    private HomeViewModel homeViewModel;
    private View view;

    private WalletCardAdapter walletAdapter;
    private WalletViewModel walletViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setApplication(this.getActivity().getApplication());
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
/*
        AnyChartView gaugeChartView = view.findViewById(R.id.home_budget_chart);
        gaugeChartView.setProgressBar(view.findViewById(R.id.home_budget_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(gaugeChartView);

        Map<String, Integer> lineData = new HashMap<>();
        lineData.put("Primo", 90);
        lineData.put("Secondo", 15);
        lineData.put("Terzo", 90);
        lineData.put("Quarto", 15);
        lineData.put("Quinto", 90);

        Chart circularGauge = new CircularGaugeChart(gaugeChartView, lineData, "Your current budgets status");
        circularGauge.instantiateChart();*/

        return this.view;
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
            this.updateDbData(activity);
            FloatingActionButton fab = view.findViewById(R.id.fab_add);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.action_nav_home_to_newTransactionTabFragment);
                }
            });
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.wallet_recycler_view);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        walletAdapter = new WalletCardAdapter(activity, listener);
        recyclerView.setAdapter(walletAdapter);
    }

    private void generateChart(Map<String, Integer> budgetLeftover) {
        AnyChartView gaugeChartView = view.findViewById(R.id.home_budget_chart);
        gaugeChartView.setProgressBar(view.findViewById(R.id.home_budget_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(gaugeChartView);
        Chart circularGauge = new CircularGaugeChart(gaugeChartView, budgetLeftover, "Your current budgets status");
        circularGauge.instantiateChart();
    }

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            walletViewModel.select(walletAdapter.getWallet(position));
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_wallet_details_fragment);
        }
    }

    private void updateDbData(Activity activity) {
        BudgetViewModel budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(BudgetViewModel.class);
        budgetViewModel.getBudgetList().observe((LifecycleOwner) activity, budgets -> {
            // Update budgets if needed
            budgetViewModel.update(budgets);
            // Retrive data from viewModel for budget's leftover
            Map<String, Integer> budgetLeftover = budgetViewModel.calculateBudgetsLeftover(budgetViewModel.getBudgetList().getValue());
            generateChart(budgetLeftover);
            // Remove observer to avoid other unnecessary updates
            budgetViewModel.getBudgetList().removeObservers((LifecycleOwner) activity);
        });

        RoutineViewModel routineViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(RoutineViewModel.class);
        routineViewModel.getRoutineList().observe((LifecycleOwner) activity, routines -> {
            routineViewModel.update(routines);
            routineViewModel.getRoutineList().removeObservers((LifecycleOwner) activity);
        });
    }
}
