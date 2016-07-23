package rishabhbanga.nanodegree.tnimdb.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import rishabhbanga.nanodegree.tnimdb.retrofit.RetrofitManager;

/**
 * Created by erishba on 7/22/2016.
 */

public class Movie implements Parcelable
{
        private static final String TAG = Movie.class.getSimpleName();
        @SerializedName("id")
        public int id;

        @SerializedName("overview")
        public String overview;

        @SerializedName("release_date")
        public String releaseDate;

        @SerializedName("poster_path")
        public String posterPath;

        @SerializedName("popularity")
        public float popularity;

        @SerializedName("title")
        public String title;

        @SerializedName("vote_average")
        public float voteAverage;

        @SerializedName("vote_count")
        public int voteCount;

        private List<Movie> movieArrayList;
        private RetrofitManager retrofitManager;

        public Movie() {
            retrofitManager = RetrofitManager.getInstance();
        }

        protected Movie(Parcel in) {
            id = in.readInt();
            overview = in.readString();
            releaseDate = in.readString();
            posterPath = in.readString();
            popularity = in.readFloat();
            title = in.readString();
            voteAverage = in.readFloat();
            voteCount = in.readInt();
        }

        public static final Creator<Movie> CREATOR = new Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel in) {
                return new Movie(in);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(overview);
            dest.writeString(releaseDate);
            dest.writeString(posterPath);
            dest.writeFloat(popularity);
            dest.writeString(title);
            dest.writeFloat(voteAverage);
            dest.writeInt(voteCount);
        }

        public List<Movie> getMovies(String moviesCategories, int pageNumber) {
            return movieArrayList;
        }
}
