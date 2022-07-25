package com.zehren.foodrater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class FoodAdapter(var context: MainActivity) : RecyclerView.Adapter<FoodViewHolder>(), ItemTouchHelperAdapter {
    private val foods = ArrayList<Food>()
    private val availableFoods = AvailableFoods()

    override fun getItemCount() = foods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view, parent, false)
        return FoodViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foods[position])
    }

    override fun onItemDismiss(position: Int) {
        delete(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        TODO("Not yet implemented")
    }

    public fun addRandom(){
        if (!availableFoods.availability.contains(true)){
            context.snackMessage("No more foods to show.")
        }else{
            var available = false
            var index = -1
            while (!available){
                index =  Random.nextInt(0, 8)
                Log.d("MSG", "Trying index $index")
                available = availableFoods.availability[index]
            }
            availableFoods.availability[index] = !availableFoods.availability[index]
            add(Food(availableFoods.names[index], availableFoods.pics[index], 0), 0)
        }
    }

    private fun add(food: Food, position: Int){
        foods.add(0, food)
        notifyItemInserted(position)
        context.recycler_view.layoutManager?.scrollToPosition(0)
        context.snackMessage("Added ${food.name}")
    }

    private fun delete(position: Int){
        var oldFood = foods[position].copy()
        foods.removeAt(position)
        availableFoods.availability[availableFoods.names.indexOf(oldFood.name)] = true
        context.snackMessage("Deleted ${oldFood.name}", "UNDO", View.OnClickListener {
            foods.add(position, oldFood)
            availableFoods.availability[availableFoods.names.indexOf(oldFood.name)] = false
            this.notifyItemInserted(position)
            context.snackMessage("Replaced ${oldFood.name}")
        })
        notifyItemRemoved(position)
    }

    fun updateRating(position: Int, rating: Int){
        foods[position].rating = rating
    }
}