package com.innorb.recipeapp.rest;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import com.innorb.recipeapp.data.DataUtils;
import com.innorb.recipeapp.idling.SimpleIdlingResource;
import com.innorb.recipeapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestExecute {

    private final RestManager restManager;
    private ArrayList<Recipe> mRecipeArrayList;

    public RestExecute() {
        mRecipeArrayList = new ArrayList<>();
        restManager = RestManager.getInstance();
    }

    public void loadData(final RestData myCallBack, final SimpleIdlingResource simpleIdlingResource) {
        if(simpleIdlingResource!=null){
            simpleIdlingResource.setIdleState(false);
        }

        Callback<ArrayList<Recipe>> callback = new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {

                mRecipeArrayList = response.body();

                if (response.isSuccessful()) {
                    myCallBack.onRestData(mRecipeArrayList);
                    if(simpleIdlingResource!=null){
                        simpleIdlingResource.setIdleState(true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                call.cancel();
                if (call.isCanceled()) {
                    myCallBack.onErrorData(t);
                }
            }
        };
        restManager.getUdacityRecipe(callback);

    }

    public void syncData(final Context context) {
        Callback<ArrayList<Recipe>> callback = new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {

                mRecipeArrayList = response.body();

                if (response.isSuccessful()) {
                    DataUtils dataUtils = new DataUtils(context);
                    dataUtils.saveDB(mRecipeArrayList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                call.cancel();
            }
        };
        restManager.getUdacityRecipeSync(callback);

    }

    public void cancelRequest() {
        if (restManager != null) {
            restManager.cancelRequest();
        }
    }

    public interface RestData {
        void onRestData(ArrayList<Recipe> listenerData);
        void onErrorData(Throwable t);
    }
}