package com.simoale.debitcredit.ui.transactions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;

/**
 * A ViewHolder describes an item view and the metadata about its place within the RecyclerView.
 */
public class TransactionCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView description;
    private TextView date;
    private TextView amount;
    private ImageView more;

    private OnItemListener itemListener;

    public TransactionCardViewHolder(@NonNull View view, OnItemListener lister) {
        super(view);
        description = view.findViewById(R.id.transaction_name);
        date = view.findViewById(R.id.transaction_date);
        amount = view.findViewById(R.id.transaction_amount);
        more = view.findViewById(R.id.transaction_more);

        itemListener = lister;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getAmount() {
        return amount;
    }

    public ImageView getMore() {
        return more;
    }

    public OnItemListener getItemListener() {
        return itemListener;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public void setAmount(TextView amount) {
        this.amount = amount;
    }
}
