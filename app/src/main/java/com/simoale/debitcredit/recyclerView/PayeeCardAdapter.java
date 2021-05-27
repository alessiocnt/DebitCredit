package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Payee;
import com.simoale.debitcredit.ui.payee.PayeeViewModel;
import com.simoale.debitcredit.ui.tag.EditTagCardViewHolder;
import com.simoale.debitcredit.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class PayeeCardAdapter extends RecyclerView.Adapter<EditTagCardViewHolder> {

    private List<Payee> payeeList = new ArrayList<>();
    private Activity activity;
    private PayeeViewModel payeeViewModel;

    public PayeeCardAdapter(Activity activity) {
        this.activity = activity;
        this.payeeViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PayeeViewModel.class);
    }


    @NonNull
    @Override
    public EditTagCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tag,
                parent, false);
        return new EditTagCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull EditTagCardViewHolder holder, int position) {
        Payee payee = payeeList.get(position);
        holder.getTagName().setText(payee.getName());
        holder.getMore().setOnClickListener(v -> {
            View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            editText.setText(payee.getName());
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("Edit payee");
            dialogBuilder.setMessage("Change payee name")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            payeeViewModel.editPayee(payee, new Payee(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Payee name cannot be empty", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                    .setNeutralButton("Delete", (dialog, id) -> {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                        alertDialogBuilder.setCancelable(false)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to delete this payee?")
                                .setPositiveButton("Delete", (dialog1, which) -> Log.e("Time to", "DELETE pay"))
                                .setNegativeButton("Cancel", (dialog1, which) -> Log.e("Time to", "NON DELETE pay"))
                                .create().show();
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return payeeList.size();
    }

    public void setData(List<Payee> payeeList) {
        this.payeeList = payeeList;
        notifyDataSetChanged();
    }

    public Payee getPayee(int position) {
        return payeeList.get(position);
    }
}
