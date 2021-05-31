package com.simoale.debitcredit.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.utils.Utilities;

import static android.content.Context.MODE_PRIVATE;

public class ChangePasswordFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout pwd1 = view.findViewById(R.id.new_password_layout);
        TextInputLayout pwd2 = view.findViewById(R.id.confirm_password_layout);
        Button save = view.findViewById(R.id.change_password_save_button);
        Button cancel = view.findViewById(R.id.change_password_cancel_button);
        save.setOnClickListener(v -> {
            String p1;
            String p2;
            p1 = pwd1.getEditText().getText().toString();
            p2 = pwd2.getEditText().getText().toString();
            if (Utilities.checkDataValid(p1, p2)) {
                if (p1.equals(p2)) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("pwd", MODE_PRIVATE);
                    preferences.edit().putString("password", p1).commit();
                    Navigation.findNavController(v).navigate(R.id.action_changePasswordFragment_to_nav_settings);
                } else {
                    Toast.makeText(getActivity(), "Password does not match", Toast.LENGTH_LONG);
                }
            } else {
                Toast.makeText(getActivity(), "Password must be not empty", Toast.LENGTH_LONG);
            }
        });
        cancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_changePasswordFragment_to_nav_settings));
    }
}
