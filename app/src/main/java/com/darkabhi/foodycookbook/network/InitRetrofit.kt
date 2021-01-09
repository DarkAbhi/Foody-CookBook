package com.darkabhi.foodycookbook.network

import com.darkabhi.foodycookbook.api.FoodApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Abhishek AN on 1/9/2021.
 */
class InitRetrofit {
    private val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private fun init(): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun instance(): FoodApi = init().create(FoodApi::class.java)
}