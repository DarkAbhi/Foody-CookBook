package com.darkabhi.foodycookbook.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Abhishek AN on 1/9/2021.
 */
data class Response(
    @field:SerializedName("meals")
    val meals: ArrayList<Food>? = null
)