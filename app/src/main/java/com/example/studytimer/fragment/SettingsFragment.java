package com.example.studytimer.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.studytimer.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}

