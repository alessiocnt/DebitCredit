package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.app.AlertDialog;
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
    private BudgetViewModel budgetViewModel;
    //list that contains all the element added by the user
    private List<Budget> budgetList = new ArrayList<>();

    public BudgetCardAdapter(Activity activity, OnItemListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(BudgetViewModel.class);
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
        Budget currentBudget = budgetList.get(position);
        holder.getName().setText(currentBudget.getName());
        holder.getRenovation().setText(String.format("Renovation date: %s", new SimpleDateFormat("dd/MM/yyyy").format(Utilities.getDateFromString(currentBudget.getDateNextUpdate()))));
        holder.getCategory().setText("Category: " + currentBudget.getCategoryName());
        holder.getBudget().setText(String.format("Leftover: %.2f€", currentBudget.getCurrentAmount()));
        holder.getMore().setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this budget?")
                    .setPositiveButton("Delete", (dialog1, which) -> {
                        if (!budgetViewModel.deleteBudget(currentBudget)) {
                            new AlertDialog.Builder(activity)
                                    .setTitle("Error")
                                    .setMessage("Cannot delete budget")
                                    .setPositiveButton("Ok", (dialog2, which1) -> dialog2.cancel())
                                    .create().show();

                        }
                    })
                    .setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel())
                    .create().show();
        });
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
