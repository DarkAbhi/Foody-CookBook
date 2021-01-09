package com.darkabhi.foodycookbook.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Abhishek AN on 1/9/2021.
 */
data class Food(
    @SerializedName("strMeal")
    val foodName: String = "",
    @SerializedName("strMealThumb")
    val foodImage: String = "",
    @SerializedName("idMeal")
    val foodId: Long = 0
)