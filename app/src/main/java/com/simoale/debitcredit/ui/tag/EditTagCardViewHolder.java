package com.simoale.debitcredit.ui.tag;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;

public class EditTagCardViewHolder extends RecyclerView.ViewHolder {

    private TextView tagName;
    private ImageView more;

    public EditTagCardViewHolder(@NonNull View view) {
        super(view);
        this.tagName = view.findViewById(R.id.tag_name);
        this.more = view.findViewById(R.id.tag_more);
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

}
