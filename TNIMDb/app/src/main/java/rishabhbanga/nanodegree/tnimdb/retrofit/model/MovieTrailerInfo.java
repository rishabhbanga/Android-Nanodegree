package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by erishba on 7/22/2016.
 */

public class MovieTrailerInfo
{
    private static final String TAG = MovieTrailerInfo.class.getSimpleName();
    @SerializedName("results")
    public List<MovieTrailer> movieTrailer;
}