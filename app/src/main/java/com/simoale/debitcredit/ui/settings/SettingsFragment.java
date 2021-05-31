package com.simoale.debitcredit.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.simoale.debitcredit.R;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialCardView changePwdBtn = view.findViewById(R.id.change_password_button);
        changePwdBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_changePasswordFragment));
        MaterialCardView editCatsBtn = view.findViewById(R.id.edit_categories_button);
        editCatsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_editCategoriesFragment));
        MaterialCardView editTagsBtn = view.findViewById(R.id.edit_tags_button);
        editTagsBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_editTagsFragment));
        MaterialCardView editPayeesBtn = view.findViewById(R.id.edit_payees_button);
        editPayeesBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_editPayeesFragment));
    }
}