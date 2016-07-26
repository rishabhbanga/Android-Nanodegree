package rishabhbanga.nanodegree.tnimdb.retrofit;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import rishabhbanga.nanodegree.tnimdb.adapter.MovieAdapterUtil;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieComments;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieTrailerInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.service.IMovieService;

/**
 * Created by erishba on 7/10/2016.
 */

public class RetrofitManager {

    public static Retrofit retrofit = null;
    public static IMovieService iMovieService = null;
    public static RetrofitManager retrofitManager = null;

    private RetrofitManager() {

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieAdapterUtil.MOVIE_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iMovieService = retrofit.create(IMovieService.class);
    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }

    //Gets movies information
    public void getMovieInfo(String categories, int page, String apiKey, Callback<MovieInfo> callback) {
        Call<MovieInfo> moviesInfoCall = iMovieService.getMoviesInfo(categories, page, apiKey);
        moviesInfoCall.enqueue(callback);
    }

    //Gets comments
    public void getComments(int movieId, String apiKey, Callback<MovieComments> callback) {
        Call<MovieComments> movieCommentsCall = iMovieService.getComments(movieId, apiKey);
        movieCommentsCall.enqueue(callback);
    }

    //Gets key for movie trailer
    public void getTrailer(int movieId, String apiKey, Callback<MovieTrailerInfo> callback) {
        Call<MovieTrailerInfo> movieCommentsCall = iMovieService.getMovieTrailer(movieId, apiKey);
        movieCommentsCall.enqueue(callback);
    }
}