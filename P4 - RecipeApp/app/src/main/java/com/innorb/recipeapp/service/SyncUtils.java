package com.innorb.recipeapp.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.innorb.recipeapp.R;
import com.innorb.recipeapp.utility.Constants;
import com.innorb.recipeapp.utility.PrefManager;

public class SyncUtils {


    private static boolean sInitialized;

    private static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context, int sync_interval) {

        if (sync_interval > 0) {
            int sync_flex = sync_interval / 2;

            Driver driver = new GooglePlayDriver(context);
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
            Job syncBakingJob = dispatcher.newJobBuilder()
                    .setService(FirebaseJobService.class)
                    .setTag(Constants.BAKING_SYNC_TAG)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(
                            sync_interval,
                            sync_interval + sync_flex))
                    .setReplaceCurrent(true)
                    .build();

            dispatcher.schedule(syncBakingJob);
        }
    }

    public static void initialize(Context context) {
        if (sInitialized) return;
        sInitialized = true;
        int interval = PrefManager.getIntGeneralSettings(context, R.string.pref_sync_frequency);
        scheduleFirebaseJobDispatcherSync(context, interval);
    }
}