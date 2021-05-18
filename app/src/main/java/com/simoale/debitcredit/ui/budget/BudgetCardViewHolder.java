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
public class BudgetCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView name;
    private TextView budget;
    private TextView renovation;
    private AnyChartView gaugeChartView;
    private ImageView more;

    private OnItemListener itemListener;

    public BudgetCardViewHolder(@NonNull View view, OnItemListener lister) {
        super(view);
        name = view.findViewById(R.id.budget_name);
        budget = view.findViewById(R.id.budget_balance);
        renovation = view.findViewById(R.id.budget_renovation);
        more = view.findViewById(R.id.wallet_more);

        gaugeChartView = view.findViewById(R.id.budget_card_chart);
        gaugeChartView.setProgressBar(view.findViewById(R.id.budget_card_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(gaugeChartView);

        itemListener = lister;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
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

    public OnItemListener getItemListener() {
        return itemListener;
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
}