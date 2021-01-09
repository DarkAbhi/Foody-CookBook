package com.darkabhi.foodycookbook.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.darkabhi.foodycookbook.databinding.ActivityMainBinding
import com.darkabhi.foodycookbook.databinding.SearchFoodDialogBinding
import com.darkabhi.foodycookbook.model.Food
import com.darkabhi.foodycookbook.model.Response
import com.darkabhi.foodycookbook.network.InitRetrofit
import com.darkabhi.foodycookbook.ui.adapter.FoodAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity(), FoodAdapter.CellClickListener {
    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private lateinit var searchFoodBinding: SearchFoodDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.favoritesSection.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    FavoritesActivity::class.java
                )
            )
        }
        binding.searchIcon.setOnClickListener {
            searchFoodBinding = SearchFoodDialogBinding.inflate(layoutInflater)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(searchFoodBinding.root)
            val dialog = builder.create()
            searchFoodBinding.searchButton.setOnClickListener SearchFood@{
                if (searchFoodBinding.searchEditText.text.toString() == "") {
                    Toast.makeText(this, "Enter food name!", Toast.LENGTH_SHORT).show()
                    return@SearchFood
                }
                startActivity(
                    Intent(this, FoodActivity::class.java).putExtra(
                        "FOOD_NAME",
                        searchFoodBinding.searchEditText.text.toString()
                    )
                )
                dialog.dismiss()
            }
            val background = ColorDrawable(Color.TRANSPARENT)
            val margin = 50
            val inset = InsetDrawable(background, margin)
            dialog.window?.setBackgroundDrawable(inset)
            dialog.show()
        }
        requestFoods()
    }

    private fun initRecyclerView(dataMeals: ArrayList<Food>) {
        dataMeals.let {
            val adapter = FoodAdapter(it, this, this, 0)
            binding.foodsRecyclerview.adapter = adapter
        }
    }

    private fun requestFoods() {
        val categories = arrayOf(
            "Beef",
            "Chicken",
            "Dessert",
            "Lamb",
            "Miscellaneous",
            "Pasta",
            "Pork",
            "Seafood",
            "Side",
            "Starter",
            "Vegan",
            "Vegetarian",
            "Goat",
            "Breakfast"
        )
        InitRetrofit().instance().requestMeals(categories.random())
            .enqueue(object : Callback<Response> {
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.printStackTrace()
                    FirebaseCrashlytics.getInstance().log(t.printStackTrace().toString())
                }

                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        val mealsList = response.body()?.meals
                        mealsList?.let {
                            initRecyclerView(it)
                        }
                    }
                }
            })
    }

    override fun onHeartClick(data: Food, position: Int) {
        val foodData = mapOf(
            "foodName" to data.foodName,
            "foodImage" to data.foodImage,
            "id" to data.foodId
        )
        db.collection("favorites")
            .document(data.foodId.toString())
            .set(foodData)
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Added to favorites", Snackbar.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().log(it.printStackTrace().toString())
            }
    }
}