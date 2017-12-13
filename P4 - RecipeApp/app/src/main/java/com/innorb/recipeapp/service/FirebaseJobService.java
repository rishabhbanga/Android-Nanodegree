package com.innorb.recipeapp.service;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.innorb.recipeapp.rest.RestExecute;

public class FirebaseJobService extends JobService {

    private RestExecute restExecute;

    @Override
    public boolean onStartJob(JobParameters job) {
        restExecute = new RestExecute();
        restExecute.syncData(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (restExecute != null) {
            restExecute.cancelRequest();
        }
        return true;
    }
}
