package com.simoale.debitcredit.ui.transactions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.simoale.debitcredit.R;

public class NewExchangeTransactionFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.new_transaction_exchange, container, false);

        return root;
    }

    public void setupUi() {
//        this.amountEditText = activity.findViewById(R.id.transaction_amount_TextInput);
//        this.descriptionEditText = activity.findViewById(R.id.transaction_description_TextInput);
//        this.dateDisplay = activity.findViewById(R.id.date_display);
//        this.noteEditText = activity.findViewById(R.id.transaction_note_TextInput);
//        this.locationText = activity.findViewById(R.id.location_text);
//        this.locationSwitch = activity.findViewById(R.id.switch_location);
//        this.captureBtn = activity.findViewById(R.id.capture_button);
//        this.imageView = activity.findViewById(R.id.imageView);
//        this.saveBtn = getView().findViewById(R.id.transaction_save_button);
//        this.cancelBtn = getView().findViewById(R.id.transaction_cancel_button);
        Log.e("aaa", "Obama Prism");
    }
}