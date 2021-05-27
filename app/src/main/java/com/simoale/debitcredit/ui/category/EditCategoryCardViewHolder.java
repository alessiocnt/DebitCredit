package com.simoale.debitcredit.ui.category;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;

public class EditCategoryCardViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryName;
    private ImageView more;

    public EditCategoryCardViewHolder(@NonNull View view) {
        super(view);
        this.categoryName = view.findViewById(R.id.category_name);
        this.more = view.findViewById(R.id.category_more);
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
}
