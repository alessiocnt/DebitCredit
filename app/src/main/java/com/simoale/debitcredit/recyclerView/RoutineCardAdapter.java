package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Routine;
import com.simoale.debitcredit.ui.routine.RoutineCardViewHolder;
import com.simoale.debitcredit.ui.routine.RoutineViewModel;
import com.simoale.debitcredit.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RoutineCardAdapter extends RecyclerView.Adapter<RoutineCardViewHolder> {
    private Activity activity;
    private OnItemListener listener;
    private RoutineViewModel routineViewModel;
    //list that contains all the element added by the user
    private List<Routine> routineList = new ArrayList<>();

    public RoutineCardAdapter(Activity activity, OnItemListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.routineViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(RoutineViewModel.class);
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     */
    @NonNull
    @Override
    public RoutineCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_routine,
                parent, false);
        return new RoutineCardViewHolder(layoutView, listener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect
     * the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull RoutineCardViewHolder holder, int position) {
        Routine currentRoutine = routineList.get(position);
        holder.getName().setText(currentRoutine.getName());
        holder.getRoutineImport().setText(String.format("%.2f€", currentRoutine.getAmount()));
        // TODO fix dates and intervals
        holder.getFrequency().setText(String.format("Repeat every: %s %s(s)", String.valueOf(currentRoutine.getRepeatNumber()), String.valueOf(currentRoutine.getRepeatInterval())));
        holder.getStartDate().setText(String.format("Starting from: %s", new SimpleDateFormat("dd/MM/yyyy").format(Utilities.getDateFromString(currentRoutine.getDate()))));
        holder.getMore().setOnClickListener(v -> {
            new AlertDialog.Builder(activity).setCancelable(false)
                    .setTitle("Delete routine")
                    .setMessage("Are you sure you want to delete this routine?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (!this.routineViewModel.deleteRoutine(currentRoutine)) {
                            new AlertDialog.Builder(activity)
                                    .setTitle("Error")
                                    .setMessage("Cannot delete routine")
                                    .setPositiveButton("Ok", (dialog1, which1) -> dialog1.cancel())
                                    .create().show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .create().show();
        });
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public void setData(List<Routine> routineList) {
        this.routineList = new ArrayList<>(routineList);
        notifyDataSetChanged();
    }

    public Routine getRoutine(int position) {
        return routineList.get(position);
    }
}
