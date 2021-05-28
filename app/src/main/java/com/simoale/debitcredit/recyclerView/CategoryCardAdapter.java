package com.simoale.debitcredit.recyclerView;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.ui.category.EditCategoryCardViewHolder;
import com.simoale.debitcredit.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class CategoryCardAdapter extends RecyclerView.Adapter<EditCategoryCardViewHolder> {

    private List<Category> categoryList = new ArrayList<>();
    private Activity activity;
    private CategoryViewModel categoryViewModel;

    public CategoryCardAdapter(Activity activity) {
        this.activity = activity;
        this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryViewModel.class);
    }

    @NonNull
    @Override
    public EditCategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category,
                parent, false);
        return new EditCategoryCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull EditCategoryCardViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.getCategoryName().setText(category.getName());
        holder.getMore().setOnClickListener(v -> {
            View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            editText.setText(category.getName());
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("Edit category");
            dialogBuilder.setMessage("Change category name")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            categoryViewModel.editCategory(category, new Category(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Category name cannot be empty", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                    .setNeutralButton("Delete", (dialog, id) -> {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
                        alertDialogBuilder.setCancelable(false)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to delete this category?")
                                .setPositiveButton("Delete", (dialog1, which) -> {
                                    if (!categoryViewModel.deleteCategory(category)) {
                                       new AlertDialog.Builder(activity, R.style.CustomAlertDialog)
                                                .setTitle("Error")
                                                .setMessage("Cannot delete category")
                                                .setPositiveButton("Ok", (dialog2, which1) -> dialog2.cancel())
                                                .create().show();

                                    }
                                })
                                .setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel())
                                .create().show();
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setData(List<Category> categoryList) {
        this.categoryList = new ArrayList<>(categoryList);
        notifyDataSetChanged();
    }

    public Category getCategory(int position) {
        return categoryList.get(position);
    }
}
