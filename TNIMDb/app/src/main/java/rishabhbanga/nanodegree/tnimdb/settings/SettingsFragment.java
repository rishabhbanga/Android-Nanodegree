package rishabhbanga.nanodegree.tnimdb.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import rishabhbanga.nanodegree.tnimdb.R;

/**
 * Created by erishba on 5/19/2016.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        addPreferencesFromResource(R.xml.preferences);

        //register event bus.
        EventBus.register(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.movies_categories_key));

    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
                //post the event of movie categories changed
                EventBus.post(new PopularMoviesEvent.PreferenceChangeEvent());
            }
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregister event bus.
        EventBus.unregister(this);
    }
}
