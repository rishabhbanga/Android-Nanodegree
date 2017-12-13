package com.innorb.recipeapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import timber.log.Timber;

class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipe.db";

    private final Context mContext;

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RECIPE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + Contract.RecipeEntry.TABLE_NAME + " (" +
                        Contract.RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.RecipeEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                        Contract.RecipeEntry.COLUMN_NAME_SERVINGS + " REAL NOT NULL, " +
                        Contract.RecipeEntry.COLUMN_NAME_IMAGE + " TEXT " +
                        ");";

        final String SQL_CREATE_INGREDIENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + Contract.IngredientEntry.TABLE_NAME + " (" +

                        BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        Contract.IngredientEntry.COLUMN_NAME_RECIPES_ID + " INTEGER NOT NULL, " +

                        Contract.IngredientEntry.COLUMN_NAME_QUANTITY + " REAL NOT NULL, " +
                        Contract.IngredientEntry.COLUMN_NAME_MEASURE + " TEXT NOT NULL, " +
                        Contract.IngredientEntry.COLUMN_NAME_INGREDIENT + " TEXT NOT NULL, " +

                        " FOREIGN KEY (" + Contract.IngredientEntry.COLUMN_NAME_RECIPES_ID + ") REFERENCES " +
                        Contract.RecipeEntry.TABLE_NAME + "(" + Contract.RecipeEntry._ID + ")," +

                        " UNIQUE (" + Contract.IngredientEntry.COLUMN_NAME_RECIPES_ID + "," + Contract.IngredientEntry.COLUMN_NAME_INGREDIENT + ")" + " ON CONFLICT REPLACE " +
                        ");";

        final String SQL_CREATE_STEP_TABLE =
                "CREATE TABLE IF NOT EXISTS " + Contract.StepEntry.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        Contract.StepEntry.COLUMN_NAME_ID + " INTEGER NOT NULL, " +
                        Contract.StepEntry.COLUMN_NAME_RECIPES_ID + " INTEGER NOT NULL, " +
                        Contract.StepEntry.COLUMN_NAME_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                        Contract.StepEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
                        Contract.StepEntry.COLUMN_NAME_VIDEO_URL + " TEXT NOT NULL, " +
                        Contract.StepEntry.COLUMN_NAME_THUMBNAIL_URL + " TEXT NOT NULL, " +

                        " FOREIGN KEY (" + Contract.StepEntry.COLUMN_NAME_RECIPES_ID + ") REFERENCES " +
                        Contract.RecipeEntry.TABLE_NAME + "(" + Contract.RecipeEntry._ID + "), " +

                        " UNIQUE (" + Contract.StepEntry.COLUMN_NAME_RECIPES_ID + "," + Contract.StepEntry.COLUMN_NAME_DESCRIPTION + ")" + " ON CONFLICT REPLACE " +
                        ");";

        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        db.execSQL(SQL_CREATE_STEP_TABLE);

        Timber.d("SQL STATEMENT:  " + SQL_CREATE_RECIPE_TABLE + " " + SQL_CREATE_INGREDIENT_TABLE + " " + SQL_CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.StepEntry.TABLE_NAME);
        onCreate(db);
        new DataUtils(mContext).clearPreferenceDb();
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.StepEntry.TABLE_NAME);
        onCreate(db);
        new DataUtils(mContext).clearPreferenceDb();
    }
}