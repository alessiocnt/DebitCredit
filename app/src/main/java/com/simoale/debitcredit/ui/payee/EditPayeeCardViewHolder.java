package com.simoale.debitcredit.ui.payee;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;

public class EditPayeeCardViewHolder extends RecyclerView.ViewHolder {

    private TextView payeeName;
    private ImageView more;

    public EditPayeeCardViewHolder(@NonNull View view) {
        super(view);
        this.payeeName = view.findViewById(R.id.payee_name);
        this.more = view.findViewById(R.id.payee_more);
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
}
