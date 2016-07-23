package rishabhbanga.nanodegree.tnimdb.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import rishabhbanga.nanodegree.tnimdb.R;

public class MovieAdapterUtil
{
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/";
    public static final String YOUTUBE_INTENT_BASE_URI = "vnd.youtube://";
    public static final String MOVIE_OBJECT = "movie_object";

    // Retrieve movie categories stored in the default sharedPreferences
    public static String getMovieCategories(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String movieCategories = sharedPreferences.getString(context.getString(R.string.movies_categories_key), context.getString(R.string.default_movies_categories));
        return movieCategories;
    }

    public  static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}