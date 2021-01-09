package com.darkabhi.foodycookbook.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.darkabhi.foodycookbook.databinding.ActivityFavoritesBinding
import com.darkabhi.foodycookbook.model.Food
import com.darkabhi.foodycookbook.ui.adapter.FoodAdapter
import com.darkabhi.foodycookbook.viewmodel.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritesActivity : AppCompatActivity(), FoodAdapter.CellClickListener {
    private lateinit var binding: ActivityFavoritesBinding
    private val db = Firebase.firestore
    private val viewModel by lazy { ViewModelProvider(this).get(FavoritesViewModel::class.java) }
    private lateinit var adapter: FoodAdapter
    private lateinit var foods: MutableList<Food>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeData()
    }

    private fun observeData() {
        viewModel.fetchFavoriteData().observe(this, {
            foods = it
            adapter = FoodAdapter(foods as ArrayList<Food>, this, this, 1)
            binding.favoritesRecyclerview.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    private fun removeFavorite(position: Int) {
        try {
            foods.removeAt(position)
            adapter.notifyItemRemoved(position)
        } catch (e: Exception) {
            Toast.makeText(this, "An internal server error occurred.", Toast.LENGTH_SHORT).show()
            FirebaseCrashlytics.getInstance().log(e.stackTrace.toString())
        }
    }

    override fun onHeartClick(data: Food, position: Int) {
        db.collection("favorites")
            .document(data.foodId.toString())
            .delete()
            .addOnSuccessListener {
                removeFavorite(position)
                Snackbar.make(binding.root, "Removed from favorites", Snackbar.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().log(it.printStackTrace().toString())
            }
    }
}