package com.simoale.debitcredit.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.simoale.debitcredit.R;

public class SettingsFragment extends PreferenceFragmentCompat {

//    private SettingsViewModel settingsViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        View root = inflater.inflate(R.layout.fragment_settings, container, false);
//
//        return root;
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_layout, rootKey);
    }
}