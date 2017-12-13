package com.innorb.recipeapp.utility;

@SuppressWarnings("ALL")
public interface Constants {

    public static final String UDACITY_BASE_URL = "http://go.udacity.com/android-baking-app-json/";

    public static final String JSON_RECIPE_ID = "id";

    public static final String JSON_RECIPE_NAME = "name";
    public static final String JSON_RECIPE_INGREDIENTS = "ingredients";
    public static final String JSON_RECIPE_STEPS = "steps";
    public static final String JSON_RECIPE_SERVINGS = "servings";
    public static final String JSON_RECIPE_IMAGE = "image";
    public static final String JSON_RECIPE_INGREDIENTS_QUANTITY = "quantity";

    public static final String JSON_RECIPE_INGREDIENTS_MEASURE = "measure";
    public static final String JSON_RECIPE_INGREDIENTS_INGREDIENT = "ingredient";
    public static final String JSON_RECIPE_STEPS_ID = "id";

    public static final String JSON_RECIPE_STEPS_SHORTDESCRIPTION = "shortDescription";
    public static final String JSON_RECIPE_STEPS_DESCRIPTION = "description";
    public static final String JSON_RECIPE_STEPS_VIDEOURL = "videoURL";
    public static final String JSON_RECIPE_STEPS_THUMBNAILURL = "thumbnailURL";


    public static final int RECIPE_LOADER_ID = 0;
    public static final int INGREDIENT_LOADER_ID = 1;
    public static final int STEP_LOADER_ID = 2;

    public static final String RECIPE_WIDGET_UPDATE = "com.innorb.recipeapp.receiver.recipe.widget.update";

    public static final int DEFAULT_TIMBER_COUNT = 3;

    public static final int TAB_ORDER_INGREDIENT = 0;
    public static final int TAB_ORDER_STEP = 1;

    public static final String EXTRA_RECIPE_IDLING = "com.innorb.recipeapp.activity.recipe.idling";

    public static final String EXTRA_RECIPE_ID = "com.innorb.recipeapp.activity.recipe.id";
    public static final String EXTRA_RECIPE_POSITION = "com.innorb.recipeapp.activity.recipe.position";
    public static final String EXTRA_TAB_ORDERTAB = "com.innorb.recipeapp.activity.tab.ordertab";

    public static final String EXTRA_RECIPE_WIDGET_ID = "com.innorb.recipeapp.activity.recipe.widget.id";
    public static final String ACTION_START_RECIPE= "com.innorb.recipeapp.widget.action.start.recipe";

    public static final String EXTRA_PROGRESSBAR_MAIN = "com.innorb.recipeapp.activity.recipe.progressbar";

    public static final String EXTRA_INGREDIENT_ID = "com.innorb.recipeapp.activity.ingredient.id";
    public static final String EXTRA_RECIPE_NAME = "com.innorb.recipeapp.activity.recipe.name";

    public static final String EXTRA_STEP_ID = "com.innorb.recipeapp.activity.step.id";
    public static final String EXTRA_STEP_TYPE_LAYOUT = "com.innorb.recipeapp.activity.step.type.layout";
    public static final String EXTRA_DETAIL_STEP_ID = "com.innorb.recipeapp.activity.detail.step.id";

    public static final String EXTRA_NAVIGATION_TYPE = "com.innorb.recipeapp.activity.navigation.type";

    public static final String BUNDLE_TAB_RECIPE_ID = "com.innorb.recipeapp.bundle.tab.recipe.id";
    public static final String BUNDLE_RECIPE_NAME = "com.innorb.recipeapp.recipe.name";
    public static final String BUNDLE_TAB_ORDERTAB = "com.innorb.recipeapp.bundle.tab.ordertab";
    public static final String BUNDLE_RECIPE_ID = "com.innorb.recipeapp.bundle.recipe.id";
    public static final String BUNDLE_RECIPE_WIDGET = "com.innorb.recipeapp.bundle.recipe.widget";

    public static final String BUNDLE_DETAIL_STEP_VIDEOURI = "com.innorb.recipeapp.activity.detail.step.videouri";
    public static final String BUNDLE_DETAIL_STEP_THUMBNAILURL = "com.innorb.recipeapp.activity.detail.step.thumbnailurl";
    public static final String BUNDLE_DETAIL_STEP_DESCRIPTION = "com.innorb.recipeapp.activity.detail.step.description";
    public static final String BUNDLE_DETAIL_STEP_SHORT_DESCRIPTION = "com.innorb.recipeapp.activity.detail.step.shortdescription";
    public static final String BUNDLE_DETAIL_STEP_ID = "com.innorb.recipeapp.activity.detail.step.id";
    public static final String BUNDLE_DETAIL_STEP_NAVIGATION_ID = "com.innorb.recipeapp.activity.detail.step.navigation.id";

    public static final String BUNDLE_EXOPLAYER_WINDOW = "com.innorb.recipeapp.activity.exoplayer.window";
    public static final String BUNDLE_EXOPLAYER_POSITION = "com.innorb.recipeapp.activity.exoplayer.position";
    public static final String BUNDLE_EXOPLAYER_AUTOPLAY = "com.innorb.recipeapp.activity.exoplayer.autoplay";

    public static final String USER_AGENT_CACHE = "CacheDataSourceFactory";
    public static final String CACHE_VIDEO_DIR = "media";
    public static final long EXT_CACHE_SIZE_MAX = 500 * 1024 * 1024;
    public static final long EXT_CACHE_FILE_SIZE_MAX = 40 * 1024 * 1024;
    public static final long CACHE_SIZE_MAX = 100 * 1024 * 1024;
    public static final long CACHE_FILE_SIZE_MAX = 20 * 1024 * 1024;

    public static final int EXO_PROGRESSBAR_DELAY = 200;

    public static final float BRIGHTNESS_COLOR_GRAYSCALE = 07f;

    public static final String BAKING_SYNC_TAG = "baking-sync";
    public static final String EXO_PLAYER_MANAGER_TAG = "StepActivity";

    public static final int NOTIFICATION_ID = 1;

    public static final String NOTIFICATION_CHANNEL_ID = "com.innorb.recipeapp.activity.exoplayer.ONE";
    public static final String NOTIFICATION_CHANNEL_NAME = "Channel Media EXOPLAYER";

    public static final int TAB_TITLE_OFFSET_DIPS = 16;
    public static final int TAB_VIEW_PADDING_DIPS = 16;
    public static final int TAB_VIEW_TEXT_SIZE_SP = 16;

    public static final int TAB_DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 2;
    public static final byte TAB_DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    public static final int TAB_SELECTED_INDICATOR_THICKNESS_DIPS = 3;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_DKGRAY = 0xFF444444;
    public static final int COLOR_GRAY = 0xFF888888;
    public static final int COLOR_LTGRAY = 0xFFCCCCCC;
    public static final int COLOR_WHITE = 0xFFFFFFFF;
    public static final int COLOR_RED = 0xFFFF0000;
    public static final int COLOR_GREEN = 0xFF00FF00;
    public static final int COLOR_BLUE = 0xFF0000FF;
    public static final int COLOR_YELLOW = 0xFFFFFF00;
    public static final int COLOR_CYAN = 0xFF00FFFF;
    public static final int TAB_DEFAULT_SELECTED_INDICATOR_COLOR = COLOR_YELLOW;

    public static final int TAB_DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
    public static final byte TAB_DEFAULT_DIVIDER_COLOR_ALPHA = 0x20;
    public static final float TAB_DEFAULT_DIVIDER_HEIGHT = 1f;

    public static final String COLOR_BACKGROUND_ACTIONBAR_OFFLINE = "#BDBDBD";

    public static final int GLIDE_IMAGE_WIDTH_RECIPE = 100;
    public static final int GLIDE_IMAGE_HEIGHT_RECIPE = 100;
    public static final float WP_GLIDE_BRIGHTNESS_RECIPE = 0.1f;

    public static final int GLIDE_BITMAP_ALPHA_STEP = 120;
    public static final float WP_GLIDE_BRIGHTNESS_STEP = 0.1f;
    public static final int GLIDE_IMAGE_WIDTH_STEP = 300;
    public static final int GLIDE_IMAGE_HEIGHT_STEP = 200;

    public static final int BITMAT_FONT_SIZE_DP = 30;

    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 10;

    public static final String PATH_SEPARATOR = "/";
}