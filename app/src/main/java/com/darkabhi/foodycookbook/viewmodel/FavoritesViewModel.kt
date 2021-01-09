package com.darkabhi.foodycookbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darkabhi.foodycookbook.model.Food
import com.darkabhi.foodycookbook.network.FavoritesRepository

/**
 * Created by Abhishek AN on 1/9/2021.
 */
class FavoritesViewModel: ViewModel() {
    private val repo = FavoritesRepository()
    fun fetchFavoriteData(): LiveData<MutableList<Food>> {
        val mutableData = MutableLiveData<MutableList<Food>>()
        repo.getFavoritesData().observeForever { foodList ->
            mutableData.value = foodList
        }
        return mutableData
    }
}