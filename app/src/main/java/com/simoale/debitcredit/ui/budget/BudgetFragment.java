package com.simoale.debitcredit.ui.budget;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.recyclerView.BudgetCardAdapter;
import com.simoale.debitcredit.recyclerView.OnItemListener;

import java.util.List;

public class BudgetFragment extends Fragment implements OnItemListener {
    private static final String LOG = "Budget-Fragment_SIMOALE";
    private View view;

    private BudgetCardAdapter budgetAdapter;
    private BudgetViewModel budgetViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetViewModel =
                new ViewModelProvider(this).get(BudgetViewModel.class);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            setRecyclerView(activity);

            budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(BudgetViewModel.class);
            //when the list of the wallets changed, the adapter gets the new list.
            budgetViewModel.getBudgetList().observe((LifecycleOwner) activity, new Observer<List<Budget>>() {
                @Override
                public void onChanged(List<Budget> budgets) {
                    budgetAdapter.setData(budgets);
                }
            });

            FloatingActionButton fab = view.findViewById(R.id.fab_add);
            fab.setOnClickListener((v) -> Navigation.findNavController(v).navigate(R.id.action_nav_budget_to_newBudgetFragment));
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.budget_recycler_view);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        budgetAdapter = new BudgetCardAdapter(activity, listener);
        recyclerView.setAdapter(budgetAdapter);
    }

    @Override
    public void onItemClick(int position) {
        // Handle the action on a click on a recyclerView element
        // Saves the current tapped item in the viewModel
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            budgetViewModel.select(budgetAdapter.getBudget(position));
            Navigation.findNavController(view).navigate(R.id.action_walletFragment_to_walletDetailsFragment);
        }
    }

}
