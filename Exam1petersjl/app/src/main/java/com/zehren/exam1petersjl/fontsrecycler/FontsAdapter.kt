package com.zehren.exam1petersjl.fontsrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zehren.exam1petersjl.FontWrapper
import com.zehren.exam1petersjl.Options
import com.zehren.exam1petersjl.R

class FontsAdapter(var context: Context, var fonts: List<FontWrapper>, var options: Options): RecyclerView.Adapter<FontsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.font_row, parent, false)
        return FontsHolder(view, this, context)
    }

    override fun getItemCount() = fonts.size

    override fun onBindViewHolder(holder: FontsHolder, position: Int) {
        holder.bind(fonts[position], options.font)
    }

    fun selectFont(position: Int){
        val oldFont = options.font
        options.font = position
        notifyItemChanged(oldFont)
        notifyItemChanged(position)
    }
}