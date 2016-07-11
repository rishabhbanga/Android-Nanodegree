package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by erishba on 7/11/2016.
 */
public class CommentsInfo {

    @SerializedName("page")
    public int page;


    @SerializedName("results")
    public List<MovieComment> movieCommentList;


    @SerializedName("total_pages")
    public int totalPage;


    @SerializedName("total_results")
    public int totalResults;
}
