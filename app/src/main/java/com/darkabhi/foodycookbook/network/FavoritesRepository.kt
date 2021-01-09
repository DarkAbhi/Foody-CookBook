package com.darkabhi.foodycookbook.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darkabhi.foodycookbook.model.Food
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by Abhishek AN on 1/9/2021.
 */
class FavoritesRepository {
    fun getFavoritesData(): LiveData<MutableList<Food>> {
        val mutableData = MutableLiveData<MutableList<Food>>()
        FirebaseFirestore.getInstance()
            .collection("favorites")
            .orderBy("foodName")
            .get()
            .addOnSuccessListener { result ->
                val listData = mutableListOf<Food>()
                for (document in result) {
                    val foodName = document.get("foodName")
                    val foodImage = document.get("foodImage")
                    val foodId = document.get("id")
                    val foodInfo = Food(
                        foodName as String,
                        foodImage as String,
                        foodId as Long
                    )
                    listData.add(foodInfo)
                }
                mutableData.value = listData
            }
        return mutableData
    }
}