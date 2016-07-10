package rishabhbanga.nanodegree.tnimdb.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieCommentEntry;

/**
 * Created by erishba on 6/18/2016.
 */

public class MovieProvider extends ContentProvider
{

    public static final int MOVIES = 101;
    public static final int MOVIES_BY_MOVIE_ID = 102;
    public static final int MOVIES_COMMENT_BY_MOVIE_ID = 103;
    public static final int MOVIES_WITH_COMMENT = 104;
    public static final int COMMENTS = 105;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final String TAG = MovieProvider.class.getSimpleName();

    private MovieDbHelper movieDbHelper = null;

    private SQLiteDatabase sqLiteDatabase;
    private static final SQLiteQueryBuilder sqliteQueryBuilder;

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    static {
        sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(MovieEntry.TABLE_NAME + " INNER JOIN "
                + MovieCommentEntry.TABLE_NAME
                + " ON "
                + MovieEntry.TABLE_NAME
                + "."
                + MovieEntry._ID
                + " = "
                + MovieCommentEntry.TABLE_NAME
                + "."
                + MovieCommentEntry.COLUMN_MOVIE_ID);

    }

    private static final String movieByMoviesId = MovieEntry.COLUMN_MOVIE_ID + " =?";

    public static final String updateMovieById = MovieEntry.COLUMN_MOVIE_ID + " =?";

    public static final String commentByMovieId = MovieCommentEntry.COLUMN_MOVIE_ID + " =?";

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        sqLiteDatabase = movieDbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                cursor = sqLiteDatabase.query(MovieEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            case MOVIES_BY_MOVIE_ID:
                cursor = getMovieId(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case MOVIES_WITH_COMMENT:
                cursor = getMoviesDetailWithComment(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case MOVIES_COMMENT_BY_MOVIE_ID:
                cursor = sqLiteDatabase.query(MovieCommentEntry.TABLE_NAME, null, commentByMovieId, selectionArgs, null, null, null);
                break;
        }
        return cursor;
    }

    private Cursor getMoviesDetailWithComment(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        sqLiteDatabase = movieDbHelper.getReadableDatabase();
        Cursor cursor = sqliteQueryBuilder.query(movieDbHelper.getReadableDatabase(), null, null, null, null, null, null);
        return cursor;
    }

    private Cursor getMovieId(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        sqLiteDatabase = movieDbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(MovieEntry.TABLE_NAME, null, movieByMoviesId, new String[]{Long.toString(MovieEntry.getMovieIdFromUri(uri))}, null, null, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case MOVIES_BY_MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIES_COMMENT_BY_MOVIE_ID:
                return MovieCommentEntry.CONTENT_TYPE;
            case MOVIES_WITH_COMMENT:
                return MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        sqLiteDatabase = movieDbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (uriMatcher.match(uri)) {
            case MOVIES: {
                long _id = sqLiteDatabase.insert(MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieEntry.buildMovieUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + returnUri);
                break;
            }
            case COMMENTS: {
                long _id = sqLiteDatabase.insert(MovieCommentEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    returnUri = MovieCommentEntry.buildCommentUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + returnUri);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                rowsDeleted = sqLiteDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COMMENTS:
                rowsDeleted = sqLiteDatabase.delete(MovieCommentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES_BY_MOVIE_ID:
                long movieId = MovieEntry.getMovieIdFromUri(uri);
                rowsDeleted = sqLiteDatabase.delete(MovieEntry.TABLE_NAME, updateMovieById, new String[]{Long.toString(movieId)});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                rowsUpdated = sqLiteDatabase.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case COMMENTS:
                rowsUpdated = sqLiteDatabase.update(MovieCommentEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        sqLiteDatabase = movieDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MOVIES: {
                sqLiteDatabase.beginTransaction();
                int returnCount = 0;

                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case COMMENTS: {
                sqLiteDatabase.beginTransaction();
                int returnCount = 0;

                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieCommentEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }


        }
        return super.bulkInsert(uri, values);
    }

    static UriMatcher buildUriMatcher() {

        //* sign matches the any string .
        //# sign matches the any integer.
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.MOVIE_PATH, MOVIES);
        uriMatcher.addURI(authority, MovieContract.MOVIE_PATH + "/#", MOVIES_BY_MOVIE_ID);
        uriMatcher.addURI(authority, MovieContract.MOVIE_COMMENTS_PATH + "/#", MOVIES_COMMENT_BY_MOVIE_ID);
        uriMatcher.addURI(authority, MovieContract.MOVIE_COMMENTS_PATH, COMMENTS);

        return uriMatcher;
    }
}
