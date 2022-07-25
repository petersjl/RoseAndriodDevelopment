package com.zehren.exam1petersjl.fontsrecycler

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zehren.exam1petersjl.FontWrapper
import com.zehren.exam1petersjl.R
import kotlinx.android.synthetic.main.font_row.view.*

class FontsHolder: RecyclerView.ViewHolder {
    val fontName: TextView = itemView.font_name
    val cardView: CardView = itemView.card_view
    lateinit var context: Context

    constructor(itemView: View, adapter: FontsAdapter, context: Context): super(itemView){
        this.context = context
        itemView.setOnClickListener{
            adapter.selectFont(adapterPosition)
        }
    }

    fun bind(font: FontWrapper, selected: Int){
        fontName.text = font.displayName
        val tf = Typeface.createFromAsset(context.assets, font.name)
        fontName.typeface = tf
        cardView.setCardBackgroundColor(if (adapterPosition == selected) ContextCompat.getColor(context, R.color.colorAccent) else Color.WHITE)
    }
}