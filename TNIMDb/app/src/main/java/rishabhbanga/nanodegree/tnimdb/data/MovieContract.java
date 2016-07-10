package rishabhbanga.nanodegree.tnimdb.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by erishba on 6/16/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "rishabhbanga.nanodegree.tnimdb";

        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String MOVIE_PATH = "movie";
    public static final String MOVIE_COMMENTS_PATH = "comments";

    //Inner class that defines the contents of the Movie table
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_POPULARITY = "movie_pop";
        public static final String COLUMN_MOVIE_VOTE = "movie_vote";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_rel_date";
        public static final String COLUMN_MOVIE_DESC = "movie_desc";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_ppath";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "movie_dpath";
        public static final String COLUMN_MOVIE_HAS_VIDEO = "movie_video";
        public static final String COLUMN_SORT = "sort_by";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildMovieIdUri(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_MOVIE_ID, String.valueOf(movieId)).build();
        }

        public static Uri buildMovieListSortedByPreferenceUri(String sort) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_SORT, sort).build();
        }

        public static int getMovieIdFromUri(Uri uri) {

            String movieId = uri.getQueryParameter(COLUMN_MOVIE_ID);
            if (movieId != null && movieId.length() > 0)
                return Integer.parseInt(movieId);
            else
                return 0;
        }

        public static String getSortFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_SORT);
        }

    }

    /* Inner class that defines the table contents of the movies comments table */
    public static final class MovieCommentEntry implements BaseColumns {

        //movies comments table name
        public static final String TABLE_NAME = "comments";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_COMMENTS_PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_COMMENTS_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_COMMENTS_PATH;


        //column for storing the movie comment
        public static final String COLUMN_MOVIE_COMMENT = "comment";
        //column with the foreign key into the movies table.
        public static final String COLUMN_MOVIE_ID = "movie_id";
        //name of movie commenter
        public static final String COLUMN_AUTHOR_NAME = "author";

        public static Uri buildCommentUri(long movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

        public static String getInsertedCommentId(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }
}