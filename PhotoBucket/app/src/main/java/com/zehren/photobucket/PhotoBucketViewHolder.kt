package com.zehren.photobucket

import android.content.Context
import android.graphics.Color
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_detail.view.*
import kotlinx.android.synthetic.main.row_view.view.*
import org.w3c.dom.Text

class PhotoBucketViewHolder: RecyclerView.ViewHolder{
    val captionTextView: TextView = itemView.image_caption
    val urlTextView: TextView = itemView.url_text
    lateinit var context: Context

    constructor(itemView: View, adapter: PhotoBucketAdapter, context: Context) : super(itemView){
        this.context = context
        itemView.setOnClickListener{
            adapter.showDetail(adapterPosition)
        }
        itemView.setOnLongClickListener {
            adapter.showPicInfoDialog(adapterPosition)
            true
        }
        itemView.setOnDragListener { v: View?, event: DragEvent? ->
            adapter.remove(adapterPosition)
            true
        }
    }

    fun bind(pic: Pic){
        captionTextView.text = pic.caption
        urlTextView.text = pic.url
    }
}