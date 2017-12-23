package com.innorb.builditbigger;

import android.content.Context;
import android.os.AsyncTask;

import com.innorb.jokebackend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;


/**
 * Created by erishba on 12/17/2017.
 */

public class EndPointsAsync extends AsyncTask<MainActivityFragment, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private MainActivityFragment mainActivityFragment;

    @Override
    protected String doInBackground(MainActivityFragment... mainActivityFragments) {

        mainActivityFragment = mainActivityFragments[0];
        context = mainActivityFragment.getActivity();

        if (myApiService == null)
        {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://social-media-metrics-5131.appspot.com/_ah/api/");
            myApiService = builder.build();
        }

        try {
            return myApiService.crackJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mainActivityFragment.loadedJoke = result;
        mainActivityFragment.launchCrackJokeActivity();
    }
}