package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import retrofit.Response;
import retrofit.Retrofit;
import rishabhbanga.nanodegree.tnimdb.BuildConfig;
import rishabhbanga.nanodegree.tnimdb.retrofit.RetrofitManager;

/**
 * Created by erishba on 7/11/2016.
 */

public class MovieTrailer

{
    private static final String TAG = MovieTrailer.class.getSimpleName();
    @SerializedName("key")
    public String key;
    private RetrofitManager retrofitManager;

    public MovieTrailer()
    {
        retrofitManager=RetrofitManager.getInstance();
    }

    public String getMovieTrailerKey(int movieId) {
        final String[] result = {null};
        retrofit.Callback<MovieTrailerInfo> movieTrailerInfoCallback = new retrofit.Callback<MovieTrailerInfo>() {
            @Override
            public void onResponse(Response<MovieTrailerInfo> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body().movieTrailer.size() > 0) {
                    Log.e(TAG, "key" + response.body().movieTrailer.get(0).key);
                    //playTrailer(response.body().movieTrailers.get(0).key);
                    result[0] = response.body().movieTrailer.get(0).key;

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        retrofitManager.getTrailer(movieId, BuildConfig.MOVIE_API_KEY, movieTrailerInfoCallback);
        return result[0];
    }
}