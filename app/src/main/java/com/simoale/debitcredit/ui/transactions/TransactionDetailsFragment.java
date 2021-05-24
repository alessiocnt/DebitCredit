package com.simoale.debitcredit.ui.transactions;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.card.MaterialCardView;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.utils.Utilities;

import java.text.SimpleDateFormat;

public class TransactionDetailsFragment extends Fragment {

    private static final String LOG = "Transaction-Details-Fragment_SIMOALE";
    private TransactionViewModel transactionViewModel;
    private TextView transactionNameTextView;
    private TextView transactionDateTextView;
    private TextView transactionAmountTextView;
    private TextView categoryTextView;
    private TextView tagsTextView;
    private TextView notesTextView;
    private TextView positionTextView;
    private ImageView image;
    private MaterialCardView imgCard;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transaction_details, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.transactionNameTextView = view.findViewById(R.id.transaction_details_name);
            this.transactionDateTextView = view.findViewById(R.id.transaction_details_date);
            this.transactionAmountTextView = view.findViewById(R.id.transaction_details_amount);
            this.categoryTextView = view.findViewById(R.id.transaction_details_category_textView);
            this.tagsTextView = view.findViewById(R.id.transaction_details_tag_textView);
            this.notesTextView = view.findViewById(R.id.transaction_details_notes);
            this.positionTextView = view.findViewById(R.id.transaction_details_position);
            this.image = view.findViewById(R.id.transaction_details_img);
            this.imgCard = view.findViewById(R.id.transaction_details_transaction_photos);

            this.transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            Transaction transaction = this.transactionViewModel.getSelected().getValue();
            this.transactionNameTextView.setText(transaction.getDescription());
            this.transactionDateTextView.setText(new SimpleDateFormat("dd/MM/yyyy").format(Utilities.getDateFromString(transaction.getDate())));
            this.transactionAmountTextView.setText(String.format("%s€", transaction.getAmount()));
            this.categoryTextView.setText(String.format("Category: %s", transaction.getCategoryName()));
            this.tagsTextView.setText("Tags: "); // TODO complete
            if (transaction.getNote() != null && !transaction.getNote().equals("")) {
                this.notesTextView.setText(String.format("Notes: %s", transaction.getNote()));
            }
            if (transaction.getLocation() != null && !transaction.getLocation().equals("")) {
                this.positionTextView.setText(String.format("Location: %s", transaction.getLocation()));
            }
            if (!transaction.getImage().equals("ic_launcher_foreground")) {
                this.image.setImageBitmap(Utilities.getImageBitmap(activity, Uri.parse(transaction.getImage())));
            } else {
                this.imgCard.setVisibility(View.INVISIBLE);
            }

//            ic_launcher_foreground
        } else {
            Log.e(LOG, "Activity is null");
        }
    }
}
