package rishabhbanga.nanodegree.tnimdb.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import rishabhbanga.nanodegree.tnimdb.app.MyMovie;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.FavMovieEntry;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;

/**
 * Created by erishba on 6/18/2016.
 */

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static MovieDbHelper mOpenHelper;

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;
    private static final int MOVIE_SORT = 102;

    private static final int MY_FAV_MOVIE = 300;
    private static final int MY_FAV_MOVIE_ID = 301;

    private static final SQLiteQueryBuilder sMyFavoritMoveQueryBuilder;

    static {
        sMyFavoritMoveQueryBuilder = new SQLiteQueryBuilder();
        sMyFavoritMoveQueryBuilder.setTables(
                MovieEntry.TABLE_NAME + " INNER JOIN " +
                        FavMovieEntry.TABLE_NAME +
                        " ON " + MovieEntry.TABLE_NAME +  // Specify the column for _ID
                        "." + MovieEntry.COLUMN_MOVIE_ID + // with the table name to differentiate
                        " = " + FavMovieEntry.TABLE_NAME +
                        "." + FavMovieEntry.COLUMN_MOVIE_ID);
    }

    private static final String sMovieIdSelection =
            MovieEntry.TABLE_NAME +
                    "." + MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMovieSortedSelection =
            MovieEntry.TABLE_NAME +
                    "." + MovieEntry.COLUMN_SORT + " = ? ";

    private Cursor getMyFavMovie(Uri uri, String[] projection, String sortOrder)
    {
        int movieId = MovieEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = null;
        String selection = null;

        if (movieId != 0) {
            selection = sMovieIdSelection;
            selectionArgs = new String[]{Integer.toString(movieId)};
        } else {

        }

        return sMyFavoritMoveQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {
        //Instance Variable for DB Helper
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;

        String sortBy = MyMovie.getPreferredSortBy(getContext());

        int movieId = MovieEntry.getMovieIdFromUri(uri);

        switch (sUriMatcher.match(uri)) {

            // movie/*
            case MOVIE_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        sMovieIdSelection,
                        new String[]{Integer.toString(movieId)},
                        null,
                        null,
                        sortOrder
                );
                break;

            // movie
            case MOVIE:
            case MOVIE_SORT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        sMovieSortedSelection,
                        new String[]{sortBy},
                        null,
                        null,
                        sortOrder
                );
                break;

            // my favorite movie/*
            case MY_FAV_MOVIE_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavMovieEntry.TABLE_NAME,
                        projection,
                        FavMovieEntry.COLUMN_MOVIE_ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            // my favorite movie
            case MY_FAV_MOVIE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Cursor to register a content observer to watch for changes in this uri and its descendants
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    // Return the Mime Type for the given URI
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
            case MOVIE_SORT:
                return MovieEntry.CONTENT_TYPE;
            case MY_FAV_MOVIE_ID:
                return FavMovieEntry.CONTENT_ITEM_TYPE;
            case MY_FAV_MOVIE:
                return FavMovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MY_FAV_MOVIE: {
                long _id = db.insert(FavMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavMovieEntry.buildMyFavMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MY_FAV_MOVIE:
                rowsDeleted = db.delete(
                        FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MY_FAV_MOVIE_ID:
                rowsUpdated = db.update(FavMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found. The code passed into the constructor represents the code to return for the root
        // URI. It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_SORT);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE, MY_FAV_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE + "/#", MY_FAV_MOVIE_ID);

        return matcher;
    }
}