package com.simoale.debitcredit.ui.payee;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;

public class EditPayeeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView payeeName;
    private ImageView more;
    private OnItemListener itemListener;

    public EditPayeeCardViewHolder(@NonNull View view, OnItemListener listener) {
        super(view);
        this.payeeName = view.findViewById(R.id.payee_name);
        this.more = view.findViewById(R.id.payee_more);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }

    public TextView getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(TextView payeeName) {
        this.payeeName = payeeName;
    }

    public ImageView getMore() {
        return more;
    }

    public void setMore(ImageView more) {
        this.more = more;
    }

    public OnItemListener getItemListener() {
        return itemListener;
    }

}
