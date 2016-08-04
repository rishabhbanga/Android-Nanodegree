package rishabhbanga.nanodegree.tnimdb.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import rishabhbanga.nanodegree.tnimdb.BuildConfig;
import timber.log.Timber;

/**
 * Created by erishba on 7/22/2016.
 */

public class BaseMovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
