package com.darkabhi.foodycookbook.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import coil.load
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.darkabhi.foodycookbook.databinding.ActivityFoodBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.json.JSONArray

class FoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener { onBackPressed() }
        val name = intent.getStringExtra("FOOD_NAME")!!
        val request = JsonObjectRequest(
            Request.Method.GET,
            "https://www.themealdb.com/api/json/v1/1/search.php?s=$name",
            null,
            {
                if (it != null) {
                    binding.linkSource.visibility = View.VISIBLE
                    binding.youtubeSource.visibility = View.VISIBLE
                    val jsonArray: JSONArray = it.getJSONArray("meals")
                    for (i in 0 until jsonArray.length()) {
                        val foodItem = jsonArray.getJSONObject(i)
                        binding.foodNameHeader.text = foodItem.getString("strMeal")
                        binding.foodInstructions.text =
                            "Instructions:\n${foodItem.getString("strInstructions")}"
                        binding.linkSource.setOnClickListener {
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(
                                this,
                                Uri.parse(foodItem.getString("strSource"))
                            )
                        }
                        binding.youtubeSource.setOnClickListener {
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(
                                this,
                                Uri.parse(foodItem.getString("strYoutube"))
                            )
                        }
                        binding.foodImage.load(foodItem.getString("strMealThumb"))
                    }
                    binding.progressWheel.visibility = View.GONE
                }
            },
            {
                FirebaseCrashlytics.getInstance()
                    .log("${it.networkResponse.statusCode}\n${it.networkResponse.data}")
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}