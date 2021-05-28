package com.simoale.debitcredit.ui.budget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;

/**
 * A ViewHolder describes an item view and the metadata about its place within the RecyclerView.
 */
public class BudgetCardViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView budget;
    private TextView category;
    private TextView renovation;
    private AnyChartView gaugeChartView;
    private ImageView more;


    public BudgetCardViewHolder(@NonNull View view, OnItemListener lister) {
        super(view);
        name = view.findViewById(R.id.budget_card_name);
        budget = view.findViewById(R.id.budget_card_balance);
        category = view.findViewById(R.id.budget_card_category);
        renovation = view.findViewById(R.id.budget_card_renovation);
        more = view.findViewById(R.id.budget_card_more);
        APIlib.getInstance().setActiveAnyChartView(gaugeChartView);
    }


    public AnyChartView getGaugeChartView() {
        return gaugeChartView;
    }

    public void setGaugeChartView(AnyChartView gaugeChartView) {
        this.gaugeChartView = gaugeChartView;
    }

    public TextView getName() {
        return name;
    }

    public TextView getBudget() {
        return budget;
    }

    public TextView getRenovation() {
        return renovation;
    }

    public ImageView getMore() {
        return more;
    }

    public TextView getCategory() {
        return category;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public void setBudget(TextView budget) {
        this.budget = budget;
    }

    public void setRenovation(TextView renovation) {
        this.renovation = renovation;
    }

    public void setCategory(TextView category) {
        this.category = category;
    }
}
