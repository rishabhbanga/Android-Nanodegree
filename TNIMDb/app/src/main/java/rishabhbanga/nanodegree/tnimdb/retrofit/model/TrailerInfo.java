package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by erishba on 7/11/2016.
 */

public class TrailerInfo {

    private static final String TAG = TrailerInfo.class.getSimpleName();
    @SerializedName("results")
    public List<Trailer> movieTrailers;
}