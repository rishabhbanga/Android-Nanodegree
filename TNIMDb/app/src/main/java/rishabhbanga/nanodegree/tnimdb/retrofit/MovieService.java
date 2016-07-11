package rishabhbanga.nanodegree.tnimdb.retrofit;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.CommentsInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.TrailerInfo;


/**
 * Created by erishba on 7/11/2016.
 */

public interface MovieService {
    @GET("3/movie/{categories}")
    Call<MovieInfo> getMoviesInfo(@Path("categories") String categories, @Query("page") int page, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<CommentsInfo> getComments(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<TrailerInfo> getMovieTrailer(@Path("id") int id, @Query("api_key") String apiKey);
}
