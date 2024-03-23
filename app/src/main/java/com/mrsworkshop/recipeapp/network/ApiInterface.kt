package com.mrsworkshop.recipeapp.network

import com.mrsworkshop.recipeapp.apiData.response.RecipeListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/")
    fun getRecipeList(
        @Query("q") searchQuery: String?,
    ): Call<RecipeListDTO>?
}