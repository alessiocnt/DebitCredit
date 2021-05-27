package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.ui.budget.BudgetCardViewHolder;
import com.simoale.debitcredit.ui.budget.BudgetViewModel;
import com.simoale.debitcredit.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     */
    @NonNull
    @Override
    public BudgetCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_budget,
                parent, false);
        return new BudgetCardViewHolder(layoutView, listener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect
     * the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull BudgetCardViewHolder holder, int position) {
        BudgetViewModel budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(BudgetViewModel.class);
        Budget currentBudget = budgetList.get(position);

        holder.getName().setText(currentBudget.getName());
//        Utilities.getDateFromString(currentBudget.getDateNextUpdate());
        holder.getRenovation().setText(String.format("Renovation date: %s", new SimpleDateFormat("dd/MM/yyyy").format(Utilities.getDateFromString(currentBudget.getDateNextUpdate()))));
        holder.getCategory().setText(currentBudget.getCategoryName());
        //LiveData<Float> budgetLeftover = budgetViewModel.calculateBudgetLeftover(currentBudget);
        //holder.getBudget().setText(String.format("%.2f€", budgetLeftover));


        //holder.getBudget().setText(budgetLeftover.toString());


        /**
         Map<String, Integer> data = new HashMap<>();
         Date lasteBudgetRefresh = new Date(System.currentTimeMillis());
         data.put(currentBudget.getName(), new TransactionRepository(activity.getApplication()).getBudgetSpent(currentBudget.getCategoryId(), )));
         Chart circularGauge = new CircularGaugeChart(holder.getGaugeChartView(), data, null);
         circularGauge.instantiateChart();**/
        // TODO calcolare laq data del prossimo budget!!!!
        //holder.getRenovation().setText(String.valueOf(currentBudget.getDate()));
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
