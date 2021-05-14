package com.simoale.debitcredit.ui.wallet;

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
public class WalletCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ImageView image;
    private TextView name;
    private TextView balance;
    private ImageView more;

    private OnItemListener itemListener;

    public WalletCardViewHolder(@NonNull View view, OnItemListener lister) {
        super(view);
        image = view.findViewById(R.id.wallet_image);
        name = view.findViewById(R.id.wallet_name);
        balance = view.findViewById(R.id.wallet_balance);
        more = view.findViewById(R.id.wallet_more);

        itemListener = lister;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getName() {
        return name;
    }

    public TextView getBalance() {
        return balance;
    }

    public ImageView getMore() {
        return more;
    }

    public OnItemListener getItemListener() {
        return itemListener;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public void setBalance(TextView balance) {
        this.balance = balance;
    }
}
