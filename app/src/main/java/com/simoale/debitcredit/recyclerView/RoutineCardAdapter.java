package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Routine;
import com.simoale.debitcredit.ui.routine.RoutineCardViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RoutineCardAdapter extends RecyclerView.Adapter<RoutineCardViewHolder> {
    private Activity activity;
    private OnItemListener listener;
    //list that contains all the element added by the user
    private List<Routine> routineList = new ArrayList<>();

    public RoutineCardAdapter(Activity activity, OnItemListener listener) {
        this.activity = activity;
        this.listener = listener;
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
        /*
        String image_path = currentWallet.getImage();
        if (image_path.contains("ic_")) {
            Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources()
                    .getIdentifier(image_path, "drawable",
                            activity.getPackageName()));
            holder.getImage().setImageDrawable(drawable);
        } else {
            Bitmap bitmap = Utilities.getImageBitmap(activity, Uri.parse(image_path));
            if (bitmap != null){
                holder.getImage().setImageBitmap(bitmap);
            }
        }
*/
        holder.getName().setText(currentRoutine.getName());
        holder.getRoutineImport().setText(String.valueOf(currentRoutine.getAmount()));
        // TODO fix dates and intervals
        holder.getFrequency().setText(String.valueOf(currentRoutine.getRepeatInterval()));
        holder.getStartDate().setText(String.valueOf(currentRoutine.getDate()));
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
