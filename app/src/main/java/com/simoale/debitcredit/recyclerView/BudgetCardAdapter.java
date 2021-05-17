package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.database.repository.TransactionRepository;
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.budget.BudgetCardViewHolder;
import com.simoale.debitcredit.ui.graphs.Chart;
import com.simoale.debitcredit.ui.graphs.CircularGaugeChart;
import com.simoale.debitcredit.ui.wallet.WalletCardViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter linked to the RecyclerView of the wallet, that extends a custom Adapter
 */
public class BudgetCardAdapter extends RecyclerView.Adapter<BudgetCardViewHolder> {

    private Activity activity;
    private OnItemListener listener;
    //list that contains all the element added by the user
    private List<Budget> budgetList = new ArrayList<>();

    public BudgetCardAdapter(Activity activity, OnItemListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    /** Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item. */
    @NonNull
    @Override
    public BudgetCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_budget,
                parent, false);
        return new BudgetCardViewHolder(layoutView, listener);
    }

    /** Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect
     * the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull BudgetCardViewHolder holder, int position) {
        Budget currentBudget = budgetList.get(position);

        holder.getName().setText(currentBudget.getName());
        holder.getBudget().setText(String.valueOf(currentBudget.getLimit()));


        Map<String, Integer> data = new HashMap<>();
        Date lasteBudgetRefresh = new Date(System.currentTimeMillis());
        data.put(currentBudget.getName(), new TransactionRepository(activity.getApplication()).getBudgetSpent(currentBudget.getCategoryId(), )));
        Chart circularGauge = new CircularGaugeChart(holder.getGaugeChartView(), data, null);
        circularGauge.instantiateChart();
        // TODO calcolare laq data del prossimo budget!!!!
        holder.getRenovation().setText(String.valueOf(currentBudget.getDate()));
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public void setData(List<Budget> budgetList) {
        this.budgetList = new ArrayList<>(budgetList);
        notifyDataSetChanged();
    }

    public Budget getBudget(int position) {
        return budgetList.get(position);
    }

}
