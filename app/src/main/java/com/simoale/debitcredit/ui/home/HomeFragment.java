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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.WalletCardAdapter;
import com.simoale.debitcredit.ui.budget.BudgetViewModel;
import com.simoale.debitcredit.ui.graphs.Chart;
import com.simoale.debitcredit.ui.graphs.CircularGaugeChart;
import com.simoale.debitcredit.ui.routine.RoutineViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;

import java.util.Map;

public class HomeFragment extends Fragment implements OnItemListener {

    private static final String LOG = "Home-Fragment_SIMOALE";

    private View view;
    private Activity activity;

    BudgetViewModel budgetViewModel;
    RoutineViewModel routineViewModel;
    private WalletCardAdapter walletAdapter;
    private WalletViewModel walletViewModel;
    private RecyclerView recyclerView;
    AnyChartView gaugeChartView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        this.activity = activity;

        this.budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(BudgetViewModel.class);
        this.routineViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(RoutineViewModel.class);

        if (activity != null) {
            setRecyclerView(activity);
            gaugeChartView = view.findViewById(R.id.home_budget_chart);
            walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            //when the list of the wallets changed, the adapter gets the new list.
            walletViewModel.getWalletList().observe((LifecycleOwner) activity, wallets -> walletAdapter.setData(wallets));
            this.updateDbData(activity);
            FloatingActionButton fab = view.findViewById(R.id.fab_add);
            fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_home_to_newTransactionTabFragment));
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

        budgetViewModel.getBudgetList().observe((LifecycleOwner) activity, budgets -> {
            // Update budgets if needed
            budgetViewModel.update(budgets);
            gaugeChartView.setVisibility(budgets.size() == 0 ? View.INVISIBLE : View.VISIBLE);
            // Retrive data from viewModel for budget's leftover
            budgetViewModel.calculateBudgetsLeftover(budgets).observe((LifecycleOwner) activity, budgetLeftover -> {
                if (budgetLeftover.values().size() != 0) {
                    generateChart(budgetLeftover);
                }
            });
        });

        RoutineViewModel routineViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(RoutineViewModel.class);
        routineViewModel.getRoutineList().observe((LifecycleOwner) activity, routines -> {
            routineViewModel.update(routines);
            routineViewModel.getRoutineList().removeObservers((LifecycleOwner) activity);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        budgetViewModel.getBudgetList().removeObservers((LifecycleOwner) activity);
        routineViewModel.getRoutineList().removeObservers((LifecycleOwner) activity);
    }
}
