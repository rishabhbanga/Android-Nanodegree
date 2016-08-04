package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by erishba on 7/11/2016.
 */

public class MovieInfo implements Parcelable
{
    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public List<Movie> movieList;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_results")
    public int totalResults;

    protected MovieInfo(Parcel in) {
        page = in.readInt();
        movieList = in.createTypedArrayList(Movie.CREATOR);
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(movieList);
        dest.writeInt(totalPages);
        dest.writeInt(totalResults);
    }
}