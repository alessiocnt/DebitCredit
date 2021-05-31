package com.simoale.debitcredit.ui.wallet;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.utils.Utilities;

import java.util.concurrent.atomic.AtomicInteger;

import dev.sasikanth.colorsheet.ColorSheet;

public class NewWalletFragment extends Fragment {

    private Button saveBtn;
    private Button cancelBtn;
    private Button selectColorBtn;
    private WalletViewModel walletViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_wallet, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            this.saveBtn = getView().findViewById(R.id.save_btn);
            this.cancelBtn = getView().findViewById(R.id.cancel_btn);
            this.selectColorBtn = getView().findViewById(R.id.select_color_btn);
            this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            TextInputLayout walletNameText = activity.findViewById(R.id.wallet_name_TextInput);
            TextInputLayout walletAmountText = activity.findViewById(R.id.wallet_initial_value);
            TextInputLayout walletDescriptionText = activity.findViewById(R.id.wallet_description);
            int[] colors = activity.getApplicationContext().getResources().getIntArray(R.array.colors);
            AtomicInteger selectedColor = new AtomicInteger(colors[0]);

            this.saveBtn.setOnClickListener(v -> {
                String walletName = walletNameText.getEditText().getText().toString();
                String walletAmount = walletAmountText.getEditText().getText().toString().replace(',', '.');
                String walletDescription = walletDescriptionText.getEditText().getText().toString();
                if (Utilities.checkDataValid(walletName, walletAmount, walletDescription)) {
                    this.walletViewModel.addWallet(new Wallet(walletName, walletDescription, Float.parseFloat(walletAmount), selectedColor.toString()));
                    Navigation.findNavController(v).navigate(R.id.action_new_wallet_to_nav_wallet);
                } else {
                    Toast.makeText(activity.getBaseContext(), "All fields are required", Toast.LENGTH_LONG).show();
                }
            });

            this.cancelBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_new_wallet_to_nav_wallet);
            });

            Drawable walletDrawable = selectColorBtn.getCompoundDrawables()[1];
            ColorSheet c = new ColorSheet().colorPicker(
                    colors,
                    selectedColor.get(),
                    false,
                    color -> {
                        selectedColor.set(color);
                        walletDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        return null;
                    }

            );
            c.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ColorSheet);
            this.selectColorBtn.setOnClickListener(v -> {
                c.show(((AppCompatActivity) activity).getSupportFragmentManager());
            });
        }
    }

    @Override
    // Needs to ensure that the current Fragment in detached from the FragmentManager
    public void onDestroy() {
        super.onDestroy();
    }

}