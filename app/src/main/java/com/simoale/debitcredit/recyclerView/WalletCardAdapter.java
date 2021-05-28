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
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.wallet.WalletCardViewHolder;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter linked to the RecyclerView of the wallet, that extends a custom Adapter
 */
public class WalletCardAdapter extends RecyclerView.Adapter<WalletCardViewHolder> {

    private Activity activity;
    private OnItemListener listener;
    private WalletViewModel walletViewModel;
    //list that contains all the element added by the user
    private List<Wallet> walletList = new ArrayList<>();

    public WalletCardAdapter(Activity activity, OnItemListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     */
    @NonNull
    @Override
    public WalletCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_wallet,
                parent, false);
        return new WalletCardViewHolder(layoutView, listener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect
     * the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull WalletCardViewHolder holder, int position) {
        Wallet currentWallet = walletList.get(position);

        String icon_color = currentWallet.getImage();
        holder.getImage().getDrawable().setTint(Integer.parseInt(icon_color));
        holder.getName().setText(currentWallet.getName());
        holder.getBalance().setText(String.format("%.2f€", currentWallet.getBalance()));
        holder.getMore().setOnClickListener(v -> {
            new AlertDialog.Builder(activity, R.style.CustomAlertDialog)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this wallet?")
                    .setPositiveButton("Delete", (dialog1, which) -> {
                        if (!walletViewModel.deleteWallet(currentWallet)) {
                            new AlertDialog.Builder(activity, R.style.CustomAlertDialog)
                                    .setTitle("Error")
                                    .setMessage("Cannot delete wallet")
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
        return walletList.size();
    }

    public void setData(List<Wallet> walletList) {
        this.walletList = new ArrayList<>(walletList);
        notifyDataSetChanged();
    }

    public Wallet getWallet(int position) {
        return walletList.get(position);
    }

}
