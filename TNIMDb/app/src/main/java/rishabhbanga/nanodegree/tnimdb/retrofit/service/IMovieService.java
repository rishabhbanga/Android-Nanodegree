package rishabhbanga.nanodegree.tnimdb.retrofit.service;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieComments;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieInfo;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieTrailerInfo;

/**
 * Created by erishba on 7/22/2016.
 */

public interface IMovieService {
    @GET("3/movie/{categories}")
    Call<MovieInfo> getMoviesInfo(@Path("categories") String categories, @Query("page") int page, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<MovieComments> getComments(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<MovieTrailerInfo> getMovieTrailer(@Path("id") int id, @Query("api_key") String apiKey);
}
