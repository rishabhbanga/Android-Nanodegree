package rishabhbanga.nanodegree.tnimdb.retrofit;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import rishabhbanga.nanodegree.tnimdb.movie.MovieAdapter;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.CommentsInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.TrailerInfo;

/**
 * Created by erishba on 7/10/2016.
 */
public class RetrofitManager {

    public static Retrofit retrofit = null;
    public static MovieService MovieService = null;
    public static RetrofitManager retrofitManager = null;

    protected RetrofitManager() {

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieAdapter.MOVIE_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService = retrofit.create(MovieService.class);
    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }

    /**
     * get movies information
     *
     * @param categories categories {popular,top_rated}
     * @param page       for getting data of specific page number
     * @param apiKey     api key that is provided by themoviedb.com
     * @param callback   callback for getting response
     */
    public void getMoviesInfo(String categories, int page, String apiKey, Callback<MovieInfo> callback) {
        Call<MovieInfo> moviesInfoCall = MovieService.getMoviesInfo(categories, page, apiKey);
        moviesInfoCall.enqueue(callback);
    }

    /**
     * gets comment of single movie having specific movieId
     *
     * @param movieId  id of movie
     * @param apiKey   api key that is provided by themoviedb.com
     * @param callback callback for getting response
     */

    public void getComments(int movieId, String apiKey, Callback<CommentsInfo> callback) {
        Call<CommentsInfo> CommentsInfoCall = MovieService.getComments(movieId, apiKey);
        CommentsInfoCall.enqueue(callback);
    }

    /**
     * gets the key for movie trailer
     *
     * @param movieId  id of movie
     * @param apiKey   api key that is provided by themoviedb.com
     * @param callback callback for getting response
     */

    public void getTrailer(int movieId, String apiKey, Callback<TrailerInfo> callback) {
        Call<TrailerInfo> CommentsInfoCall = MovieService.getMovieTrailer(movieId, apiKey);
        CommentsInfoCall.enqueue(callback);
    }
}

