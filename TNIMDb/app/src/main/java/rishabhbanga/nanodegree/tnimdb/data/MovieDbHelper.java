package rishabhbanga.nanodegree.tnimdb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieCommentEntry;

/**
 * Created by erishba on 6/18/2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE "
                        + MovieEntry.TABLE_NAME
                        + " ("
                        + MovieEntry._ID + " INTEGER PRIMARY KEY,"
                        + MovieEntry.COLUMN_MOVIE_ID + " INTEGER  NOT NULL,"
                        + MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
                        + MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL,"
                        + MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL,"
                        + MovieEntry.COLUMN_MOVIE_RATING + " REAL NOT NULL,"
                        + MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL"
                        + ");";

        //on delete cascade  restriction is for deleting comments from the comments table when deleting the movie from movie table.
        final String SQL_CREATE_MOVIE_COMMENTS_TABLE =
                "CREATE TABLE "
                        + MovieCommentEntry.TABLE_NAME
                        + " ("
                        + MovieCommentEntry._ID + " INTEGER PRIMARY KEY,"
                        + MovieCommentEntry.COLUMN_MOVIE_COMMENT + " TEXT NOT NULL,"
                        + MovieCommentEntry.COLUMN_AUTHOR_NAME + " TEXT NOT NULL,"
                        + MovieCommentEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"
                        + " FOREIGN KEY (" + MovieCommentEntry.COLUMN_MOVIE_ID + ")"
                        + " REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry.COLUMN_MOVIE_ID + ")"
                        // +" UNIQUE("+MovieCommentEntry.COLUMN_MOVIE_ID+")"
                        + " ON DELETE CASCADE"
                        + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCommentEntry.TABLE_NAME);
        onCreate(db);
    }
}

