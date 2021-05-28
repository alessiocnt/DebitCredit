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
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.ui.transactions.TransactionCardViewHolder;
import com.simoale.debitcredit.ui.transactions.TransactionViewModel;
import com.simoale.debitcredit.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter linked to the RecyclerView of the transaction, that extends a custom Adapter
 */
public class TransactionCardAdapter extends RecyclerView.Adapter<TransactionCardViewHolder> {

    private Activity activity;
    private OnItemListener listener;
    private TransactionViewModel transactionViewModel;
    //list that contains all the element added by the user
    private List<Transaction> transactionList = new ArrayList<>();

    public TransactionCardAdapter(Activity activity, OnItemListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     */
    @NonNull
    @Override
    public TransactionCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction,
                parent, false);
        return new TransactionCardViewHolder(layoutView, listener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect
     * the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull TransactionCardViewHolder holder, int position) {
        Transaction currentTransaction = transactionList.get(position);

        holder.getDescription().setText(currentTransaction.getDescription());
        holder.getDate().setText(new SimpleDateFormat("dd/MM/yyyy").format(Utilities.getDateFromString(currentTransaction.getDate())));
        holder.getAmount().setText(String.format("%.2f€", currentTransaction.getAmount()));
        holder.getMore().setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Delete", (dialog1, which) -> {
                        if (!transactionViewModel.deleteTransaction(currentTransaction)) {
                            new AlertDialog.Builder(activity)
                                    .setTitle("Error")
                                    .setMessage("Cannot delete transaction")
                                    .setPositiveButton("Ok", (dialog2, which1) -> dialog2.cancel())
                                    .create().show();

                        }
                    })
                    .setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel())
                    .create().show();
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void setData(List<Transaction> transactionList) {
        this.transactionList = new ArrayList<>(transactionList);
        notifyDataSetChanged();
    }

    public Transaction getTransaction(int position) {
        return transactionList.get(position);
    }

}
