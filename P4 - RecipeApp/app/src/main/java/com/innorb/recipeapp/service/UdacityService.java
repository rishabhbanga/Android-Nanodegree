package com.innorb.recipeapp.service;

import java.util.ArrayList;

import com.innorb.recipeapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UdacityService {
    @GET(" ")
    Call<ArrayList<Recipe>> getUdacityService();
}
