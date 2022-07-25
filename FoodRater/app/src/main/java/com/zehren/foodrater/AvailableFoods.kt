package com.zehren.foodrater

data class AvailableFoods(val availability: BooleanArray = BooleanArray(8, {true})) {
    val names = arrayOf("Banana", "Homemade Bread", "Broccoli", "Chicken", "Chocolate", "Ice Cream", "Lima Beans", "Steak")
    val pics = intArrayOf(R.drawable.banana, R.drawable.bread, R.drawable.broccoli, R.drawable.chicken, R.drawable.chocolate, R.drawable.icecream, R.drawable.limabeans, R.drawable.steak)
}