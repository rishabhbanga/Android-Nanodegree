package com.innorb.recipeapp.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.google.android.exoplayer2.util.Util;
import com.innorb.recipeapp.R;
import com.innorb.recipeapp.utility.Utility;

public class SettingsFragment extends PreferenceFragmentCompat {

    private String mAppVersionName;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general_settings);
        prefMail();
    }

    private void prefMail(){
        final Preference mailTo = findPreference("pref_contact");
        mailTo.setOnPreferenceClickListener(preference -> {
            int androidVersionCode = 0;
            String hwInfo = null;
            try {
                androidVersionCode = Util.SDK_INT;
                hwInfo = Util.MANUFACTURER + " - " + Util.MODEL;
                mAppVersionName = new Utility(getActivity(), null).appVersionName();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            String textMail = "Version: " +
                    mAppVersionName + "\n" +
                    "Model: " +
                    hwInfo + "\n" +
                    "Api: " +
                    String.valueOf(androidVersionCode)+"\n\n";

            Intent mailIntent = new Intent(Intent.ACTION_SEND);
            mailIntent.setType("message/rfc822");
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.app_support_mail)});
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_support_mail) + " " + getString(R.string.app_name));
            mailIntent.putExtra(Intent.EXTRA_TEXT, textMail);
            startActivity(Intent.createChooser(mailIntent, getString(R.string.text_mail_intent)));
            return true;
        });

    }
}