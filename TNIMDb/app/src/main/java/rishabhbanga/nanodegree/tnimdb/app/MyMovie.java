package rishabhbanga.nanodegree.tnimdb.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import rishabhbanga.nanodegree.tnimdb.R;

/**
 * Created by erishba on 5/17/2016.
 */

public class MyMovie{

    private int id;
    private String title;
    private double popularity;
    private double vote_avg;
    private String releaseDate;
    private String description;
    private String posterPath;
    private String backdropPath;
    private boolean video;

    public static final String BaseUrl = "https://image.tmdb.org/t/p/w500";
    public static final String BaseMovieUrl = "http://api.themoviedb.org/3/movie";
    public static final String BaseDiscoverUrl = "http://api.themoviedb.org/3/discover/movie?";

    public MyMovie(int id, String title, double popularity, double vote_avg,
                   String releaseDate, String description,
                   String posterPath, String backdropPath,
                   boolean video) {
        this.id = id;
        this.title = title;
        this.popularity = popularity;
        this.vote_avg = vote_avg;
        this.releaseDate = releaseDate;
        this.description = description;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.video = video;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getYear(String releaseDate){
        String year;
        int i = releaseDate.indexOf('-');
        year = releaseDate.substring(0,i);
        return year;
    }

    public static String getVote(Double vote){
        String moveVote = Double.toString(vote);
        return moveVote+"/10";
    }

    // As a placeholder
    public static String getDuration(){
        return "120min";
    }

    public static String getPreferredSortBy(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_by_key),
                context.getString(R.string.pref_sort_by_popularity));
   }
}
