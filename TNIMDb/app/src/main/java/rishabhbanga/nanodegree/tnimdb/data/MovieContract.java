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

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAV_MOVIE = "favmovie";

    //Inner class that defines the contents of the Movie table

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

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

    public static final class FavMovieEntry implements BaseColumns
        {
            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE).build();

            // Return multiple rows
            public static final String CONTENT_TYPE =
                    "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;

            // Return a single row
            public static final String CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;

            public static final String TABLE_NAME = "favmovie";
            public static final String COLUMN_MOVIE_ID = "movie_id";

            public static Uri buildMyFavMovieUri(long id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }

            public static Uri buildMyFavMovieIdUri(int movieId) {
                return CONTENT_URI.buildUpon()
                        .appendQueryParameter(COLUMN_MOVIE_ID, String.valueOf(movieId)).build();
            }
        }
    }