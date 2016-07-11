package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import rishabhbanga.nanodegree.tnimdb.BuildConfig;
import rishabhbanga.nanodegree.tnimdb.retrofit.RetrofitManager;

import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by erishba on 7/11/2016.
 */

public class Trailer {

    private static final String TAG = Trailer.class.getSimpleName();
    @SerializedName("key")
    public String key;
    protected RetrofitManager retrofitManager;

    public Trailer()
    {
        retrofitManager = RetrofitManager.getInstance();
    }

    public String getMovieTrailerKey(int movieId) {
        final String[] result = {null};
        retrofit.Callback<TrailerInfo> movieTrailerInfoCallback = new retrofit.Callback<TrailerInfo>() {
            @Override
            public void onResponse(Response<TrailerInfo> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body().movieTrailers.size() > 0) {
                    Log.e(TAG, "key" + response.body().movieTrailers.get(0).key);
                    //playTrailer(response.body().movieTrailers.get(0).key);
                    result[0] = response.body().movieTrailers.get(0).key;
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
