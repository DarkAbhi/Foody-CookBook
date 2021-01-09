package com.darkabhi.foodycookbook.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.darkabhi.foodycookbook.R
import com.darkabhi.foodycookbook.databinding.FoodItemCardBinding
import com.darkabhi.foodycookbook.model.Food

/**
 * Created by Abhishek AN on 1/9/2021.
 */
class FoodAdapter(
    private val foodList: ArrayList<Food>,
    val context: Context,
    val cellClickListener: CellClickListener,
    val origin: Int
) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FoodItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface CellClickListener {
        fun onHeartClick(data: Food,position: Int)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodList[position]
        holder.bindView(food,position)
    }

    inner class ViewHolder(private val itemBinding: FoodItemCardBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(foodData: Food,position: Int) {
            if (origin == 1)
                itemBinding.heartFood.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_heart_filled
                    )
                )
            itemBinding.foodName.text = foodData.foodName
            itemBinding.foodThumbnail.load(foodData.foodImage)
            itemBinding.heartFood.setOnClickListener {
                cellClickListener.onHeartClick(foodData,position)
                if (origin == 0)
                    itemBinding.heartFood.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_heart_filled
                        )
                    )
                else
                    itemBinding.heartFood.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_heart_unfilled
                        )
                    )
            }
        }
    }
}