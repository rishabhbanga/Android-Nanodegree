package com.innorb.recipeapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.innorb.recipeapp";

    @SuppressWarnings("WeakerAccess")
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    private Contract() {}

    public static class RecipeEntry  implements  BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SERVINGS = "servings";
        public static final String COLUMN_NAME_IMAGE = "image";

    }

    public static class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_NAME_RECIPES_ID = "recipes_id";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_MEASURE = "measure";
        public static final String COLUMN_NAME_INGREDIENT = "ingredient";

    }

    public static class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STEPS;

        @SuppressWarnings("unused")
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STEPS;

        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_NAME_ID = "id";// not primary id, udacity value only
        public static final String COLUMN_NAME_RECIPES_ID = "recipes_id";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_VIDEO_URL = "videoURL";
        public static final String COLUMN_NAME_THUMBNAIL_URL = "thumbnailURL";
    }
}