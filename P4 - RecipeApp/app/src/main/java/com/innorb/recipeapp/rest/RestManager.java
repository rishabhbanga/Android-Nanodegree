package com.innorb.recipeapp.rest;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.innorb.recipeapp.model.Recipe;
import com.innorb.recipeapp.service.UdacityService;
import com.innorb.recipeapp.utility.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static UdacityService sUdacityService;
    private static RestManager sRestManager;
    private Call<ArrayList<Recipe>> mCall;

    private RestManager() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.UDACITY_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sUdacityService = retrofit.create(UdacityService.class);
    }

    public static RestManager getInstance() {
        if (sRestManager == null) {
            sRestManager = new RestManager();
        }
        return sRestManager;
    }

    public void getUdacityRecipe(Callback<ArrayList<Recipe>> callback) {
        mCall = sUdacityService.getUdacityService();
        mCall.enqueue(callback);
    }

    public void getUdacityRecipeSync(Callback<ArrayList<Recipe>> callback) {
        Call<ArrayList<Recipe>> call = sUdacityService.getUdacityService();
        call.enqueue(callback);
    }

    public void cancelRequest() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}