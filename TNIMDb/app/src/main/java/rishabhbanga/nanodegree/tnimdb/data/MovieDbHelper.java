package rishabhbanga.nanodegree.tnimdb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by erishba on 6/16/2016.
 */

import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieCommentEntry;

public class MovieDbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    //Initializing Database Helper
    public MovieDbHelper (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //SQL and MovieContract class used together
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_VOTE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_DESC + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_HAS_VIDEO + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_SORT + " TEXT NOT NULL);";

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
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        // todo persistent the user data
        db.execSQL("DROP TABLE IF EXISTS " + MovieCommentEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
