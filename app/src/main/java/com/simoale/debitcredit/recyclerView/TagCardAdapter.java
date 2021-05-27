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
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.ui.tag.EditTagCardViewHolder;
import com.simoale.debitcredit.ui.tag.TagViewModel;
import com.simoale.debitcredit.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class TagCardAdapter extends RecyclerView.Adapter<EditTagCardViewHolder> {

    private List<Tag> tagList = new ArrayList<>();
    private Activity activity;
    private TagViewModel tagViewModel;

    public TagCardAdapter(Activity activity) {
        this.activity = activity;
        this.tagViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TagViewModel.class);
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
        Tag tag = tagList.get(position);
        holder.getTagName().setText(tag.getName());
        holder.getMore().setOnClickListener(v -> {
            View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            editText.setText(tag.getName());
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("Edit tag");
            dialogBuilder.setMessage("Change tag name")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            tagViewModel.editTag(tag, new Tag(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Tag name cannot be empty", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                    .setNeutralButton("Delete", (dialog, id) -> {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                        alertDialogBuilder.setCancelable(false)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to delete this tag?")
                                .setPositiveButton("Delete", (dialog1, which) -> Log.e("Time to", "DELETE tag"))
                                .setNegativeButton("Cancel", (dialog1, which) -> Log.e("Time to", "NON DELETE tag"))
                                .create().show();
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public void setData(List<Tag> tagList) {
        this.tagList = tagList;
        notifyDataSetChanged();
    }

    public Tag getTag(int position) {
        return tagList.get(position);
    }
}
