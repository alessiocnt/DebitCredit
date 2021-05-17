package com.simoale.debitcredit.ui.routine;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;

public class RoutineCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView name;
    private TextView routineImport;
    private TextView frequency;
    private TextView startDate;
    private ImageView more;
    private OnItemListener itemListener;

    public RoutineCardViewHolder(@NonNull View view, OnItemListener listener) {
        super(view);
        this.name = view.findViewById(R.id.routine_name);
        this.routineImport = view.findViewById(R.id.routine_import);
        this.frequency = view.findViewById(R.id.routine_frequency);
        this.startDate = view.findViewById(R.id.routine_start);
        this.more = view.findViewById(R.id.routine_more);
    }
    

    public ImageView getMore() {
        return more;
    }

    public OnItemListener getItemListener() {
        return itemListener;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getRoutineImport() {
        return routineImport;
    }

    public void setRoutineImport(TextView routineImport) {
        this.routineImport = routineImport;
    }

    public TextView getFrequency() {
        return frequency;
    }

    public void setFrequency(TextView frequency) {
        this.frequency = frequency;
    }

    public TextView getStartDate() {
        return startDate;
    }

    public void setStartDate(TextView startDate) {
        this.startDate = startDate;
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }
}
