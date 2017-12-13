package com.innorb.recipeapp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.innorb.recipeapp.R;

public class PrefManager {

    public static boolean isGeneralSettings(Context context, String key) {
        PreferenceManager.setDefaultValues(context, R.xml.pref_general_settings, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean
                (key, false);
    }

    public static int getIntGeneralSettings(Context context, @SuppressWarnings("SameParameterValue") int key) {
        PreferenceManager.setDefaultValues(context, R.xml.pref_general_settings, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.valueOf(
                sharedPref.getString(context.getString(key), "0"));
    }


    public static void clearGeneralSettings(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.pref_general_settings, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
    }

    public static void putIntPref(Context context, @SuppressWarnings("SameParameterValue") int key, int value) {
        SharedPreferences prefId = context
                .getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefId.edit();
        editor.putInt(context.getString(key), value);
        editor.apply();
    }

    public static void putStringPref(Context context, int key, String value) {
        SharedPreferences prefId = context
                .getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefId.edit();
        editor.putString(context.getString(key), value);
        editor.apply();
    }

    public static void putBoolPref(Context context, int key, @SuppressWarnings("SameParameterValue") boolean value) {
        SharedPreferences prefId = context
                .getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefId.edit();
        editor.putBoolean(context.getString(key), value);
        editor.apply();
    }

    public static int getIntPref(Context context, @SuppressWarnings("SameParameterValue") int key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(key), 0);
    }

    public static String getStringPref(Context context, int key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(key), "");
    }

    public static boolean isPref(Context context, int key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(key), false);
    }


    public static void clearPref(Context context) {
        int[] prefArrays = {R.string.pref_video_uri, R.string.pref_rendering_video, R.string.pref_tab_layout,
                R.string.pref_title_sync_frequency, R.string.pref_video_notification,
                R.string.pref_widget_id, R.string.pref_widget_name, R.string.pref_write_external_storage, R.string.pref_request_permission};

        for (int pref : prefArrays) {
            context.getSharedPreferences(context.getString(pref), 0).edit().clear().apply();
        }
    }
}