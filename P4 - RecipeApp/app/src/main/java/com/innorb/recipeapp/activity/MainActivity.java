package com.innorb.recipeapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.util.Util;
import com.innorb.recipeapp.R;
import com.innorb.recipeapp.fragment.RecipeFragment;
import com.innorb.recipeapp.idling.SimpleIdlingResource;
import com.innorb.recipeapp.rest.RestExecute;
import com.innorb.recipeapp.utility.Constants;
import com.innorb.recipeapp.utility.PrefManager;
import com.innorb.recipeapp.utility.Utility;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.innorb.recipeapp.data.DataUtils;
import com.innorb.recipeapp.model.Recipe;
import com.innorb.recipeapp.service.SyncUtils;

import timber.log.Timber;

public class MainActivity extends BaseActivity implements
        RestExecute.RestData, RecipeFragment.FragmentInteractionListener, ActivityCompat.OnRequestPermissionsResultCallback {

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.progress_bar_main)
    ProgressBar mProgressBar;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.error_text)
    TextView mErrorText;

    private Context mContext;

    private String mRecipeName;

    private int mId;
    private boolean mStateProgressBar;


    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLayoutResource(R.layout.activity_main);
        setEnableNavigationView(true);
        super.onCreate(savedInstanceState);

        getIdlingResource();

        getLayoutInflater().inflate(getLayoutResource(), getFrameLayout());

        mContext = MainActivity.this;
        ButterKnife.bind(this);


        Timber.plant(new Timber.DebugTree());

        SyncUtils.initialize(this);
        Intent intent = getIntent();
        if (intent != null) {
            startWidget(getIntent().getIntExtra(Constants.EXTRA_RECIPE_WIDGET_ID, -1),
                    getIntent().getStringExtra(Constants.EXTRA_RECIPE_NAME));

        }

        if (Util.SDK_INT > 23) {
            Utility.RequestPermissionExtStorage(MainActivity.this);
            Utility.isDeniedPermissionExtStorage(MainActivity.this);
        }

        initializeMainJob();


    }

    private void startWidget(int recipeWidgetId, String recipeName) {
        if (recipeWidgetId >= 0) {
            Intent intent;
            intent = new Intent(mContext, DetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(Constants.EXTRA_RECIPE_ID, recipeWidgetId);
            intent.putExtra(Constants.EXTRA_RECIPE_NAME, recipeName);
            setRecipeId(recipeWidgetId);
            setRecipeName(recipeName);

            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PrefManager.putBoolPref(mContext, R.string.pref_write_external_storage, true);
                    PrefManager.putBoolPref(mContext, R.string.pref_request_permission, false);
                }
            }
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        if ((!Objects.equals(permission, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                (Utility.isPermissionExtStorage(mContext) ||
                        PrefManager.isPref(mContext, R.string.pref_request_permission))) {
            return super.shouldShowRequestPermissionRationale(permission);
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        PrefManager.putBoolPref(mContext, R.string.pref_request_permission, true);
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mId = savedInstanceState.getInt(Constants.EXTRA_RECIPE_ID);
        mRecipeName = savedInstanceState.getString(Constants.EXTRA_RECIPE_NAME);
        mStateProgressBar = savedInstanceState.getBoolean(Constants.EXTRA_PROGRESSBAR_MAIN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.EXTRA_RECIPE_ID, mId);
        outState.putString(Constants.EXTRA_RECIPE_NAME, mRecipeName);
        outState.putBoolean(Constants.EXTRA_PROGRESSBAR_MAIN, mStateProgressBar);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(int id, String recipeName) {
        Intent intent;
        if (Utility.isTablet(mContext) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            intent = new Intent(mContext, StepActivity.class);
            setRecipeId(id);
        } else {
            intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Constants.EXTRA_RECIPE_ID, id);

        }
        intent.putExtra(Constants.EXTRA_RECIPE_NAME, recipeName);
        startActivity(intent);
    }

    @Override
    public void onRestData(ArrayList<Recipe> listenerData) {
        if (listenerData != null) {
            hiddenProgressBar();
            DataUtils dataUtils = new DataUtils(mContext);
            if (dataUtils.saveDB(listenerData)) {
                startFragmentDb();
            } else {
                shownError(R.string.error_state_critical, "Insert Data");
            }
        }

    }

    @Override
    public void onErrorData(Throwable throwable) {
        hiddenProgressBar();
        if ((throwable instanceof SocketTimeoutException) ||
                (throwable instanceof UnknownHostException) || (throwable instanceof ConnectException)) {
            shownError(R.string.network_state_not_connected, null);
        } else {
            shownError(R.string.error_state_critical, throwable.getMessage());
        }

    }

    public void restartNetwork(View view) {
        if (view != null) {
            if (view.getTag().equals(R.string.network_state_not_connected)) {
                view.setOnClickListener(v -> {
                    if (Utility.isOnline(mContext)) {
                        initializeMainJob();
                        v.setClickable(false);
                    }
                });
            }
        }
    }

    private void initializeMainJob() {
        if (PrefManager.isPref(mContext, R.string.pref_insert_data)) {
            new Utility(mContext, getSupportActionBar()).setColorOfflineActionBar();

            startFragmentDb();
        } else {
            showProgressBar();
            new RestExecute().loadData(this,mIdlingResource);

        }
        clearPosition();

    }

    private void startFragmentDb() {
        mErrorText.setVisibility(View.INVISIBLE);
        mStateProgressBar = false;

        RecipeFragment recipeFragment = new RecipeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_list_container, recipeFragment).commit();

    }

    private void shownError(int resourceError, String details) {
        mErrorText.setVisibility(View.VISIBLE);
        if (details == null) details = "...";
        mErrorText.setText(getString(resourceError, details));
        mErrorText.setTag(resourceError);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mStateProgressBar = true;
    }

    private void hiddenProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mStateProgressBar = false;
    }


    public static void homeActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY));
    }

}
