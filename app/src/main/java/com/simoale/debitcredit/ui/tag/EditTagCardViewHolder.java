package com.simoale.debitcredit.ui.tag;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;

public class EditTagCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView tagName;
    private ImageView more;
    private OnItemListener itemListener;

    public EditTagCardViewHolder(@NonNull View view, OnItemListener listener) {
        super(view);
        this.tagName = view.findViewById(R.id.tag_name);
        this.more = view.findViewById(R.id.tag_more);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }

    public TextView getTagName() {
        return tagName;
    }

    public void setTagName(TextView tagName) {
        this.tagName = tagName;
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
