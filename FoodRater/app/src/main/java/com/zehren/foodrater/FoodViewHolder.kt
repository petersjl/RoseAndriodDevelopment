package com.zehren.foodrater

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_view.view.*

class FoodViewHolder : RecyclerView.ViewHolder {
    val foodTextView: TextView = itemView.name
    val foodResourceView: ImageView = itemView.image
    val foodRatingView: RatingBar = itemView.rating
    lateinit var context: Context
    
    constructor(itemView: View, adapter: FoodAdapter, context: Context): super(itemView){
        this.context = context
        foodRatingView.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser){
                adapter.updateRating(adapterPosition, rating.toInt())
            }
        }
    }
    
    fun bind(food: Food){
        foodTextView.text = food.name
        foodResourceView.setImageResource(food.resource)
        foodRatingView.rating = food.rating.toFloat()
    }
}