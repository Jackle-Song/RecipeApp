package com.mrsworkshop.recipeapp.viewModel

import com.mrsworkshop.recipeapp.apiData.response.RecipeListDTO
import com.mrsworkshop.recipeapp.network.ApiClient
import com.mrsworkshop.recipeapp.network.ApiInterface
import retrofit2.Call

class RecipeApiData {
    private var apiInterface : ApiInterface? = null

    init {
        apiInterface = ApiClient.retrofitService()
    }

    fun getRecipeList(query : String?): Call<RecipeListDTO>? {
        return apiInterface?.getRecipeList(query)
    }
}