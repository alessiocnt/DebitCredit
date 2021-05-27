package com.simoale.debitcredit.ui.category;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;

public class EditCategoryCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView categoryName;
    private ImageView more;
    private OnItemListener itemListener;

    public EditCategoryCardViewHolder(@NonNull View view, OnItemListener listener) {
        super(view);
        this.categoryName = view.findViewById(R.id.category_name);
        this.more = view.findViewById(R.id.category_more);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }

    public TextView getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(TextView categoryName) {
        this.categoryName = categoryName;
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
