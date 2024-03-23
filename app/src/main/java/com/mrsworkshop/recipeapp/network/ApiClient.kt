package com.mrsworkshop.recipeapp.network

import com.mrsworkshop.recipeapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    fun retrofitService(): ApiInterface {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor()) // Add logging interceptor
            .addInterceptor(headerInterceptor()) // Add header interceptor
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_RECIPE)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private fun headerInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key", Constants.RECIPE_API_KEY)
                .addHeader("X-RapidAPI-Host", Constants.RECIPE_HOST)
                .build()
            chain.proceed(request)
        }
    }
}