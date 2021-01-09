package com.darkabhi.foodycookbook.api

import com.darkabhi.foodycookbook.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Abhishek AN on 1/9/2021.
 */
interface FoodApi {
    @GET("filter.php")
    fun requestMeals(
        @Query("c") category:String
    ) : Call<Response>
}