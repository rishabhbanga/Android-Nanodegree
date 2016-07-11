package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by erishba on 7/11/2016.
 */
public class MovieComment {
    @SerializedName("id")
    public String id;


    @SerializedName("author")
    public String author;


    @SerializedName("content")
    public String content;
}
