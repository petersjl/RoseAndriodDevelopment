package com.zehren.photobucket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import org.w3c.dom.Document

class PhotoBucketAdapter(var context: Context, var uid: String): RecyclerView.Adapter<PhotoBucketViewHolder>() {
    private var pics = ArrayList<Pic>()
    private var picsRef = FirebaseFirestore.getInstance().collection("pics")
    var listener: ListenerRegistration? = null

    init {
        setUpListener(false)
    }

    fun setUpListener(showAll: Boolean){
        Log.d("checkthis", "Adapter got show all as: $showAll")
        if (listener != null) listener!!.remove()
        pics.clear()
        notifyDataSetChanged()
        var query = picsRef.orderBy(Pic.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
        if (!showAll) {
            query = query.whereEqualTo("uid", uid)
            Log.d("checkthis", "This is the user id: $uid")
        }
        listener = query.addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            Log.d("pics", "Found a change")
            if (error != null) {
                Log.e("picerror", "Listen error: ${error.toString()}")
            }
            if (false) {
                pics.clear()
                notifyDataSetChanged()
                Log.d("pics", "Cleared array")
                for (doc in snapshot!!) {
                    pics.add(0, Pic.fromSnapshot(doc))
                    notifyItemInserted(0)
                }
            } else {
                for (docChange in snapshot!!.documentChanges){
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            pics.add(0, Pic.fromSnapshot(docChange.document))
                            notifyItemInserted(0)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val newPic = Pic.fromSnapshot(docChange.document)
                            val index = findPic(newPic)
                            if (index == -1) {
                                Log.e("picerror", "Error finding the changed pic")
                            }
                            else {
                                pics[index] = newPic
                                notifyItemChanged(index)
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            val index = findPic(Pic.fromSnapshot(docChange.document))
                            if (index == -1) {
                                Log.e("picerror", "Error finding the deleted pic")
                            }
                            else {
                                pics.removeAt(index)
                                notifyItemRemoved(index)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun findPic(pic: Pic): Int{
        for (i in 0 until pics.size) {
            if (pics[i].id == pic.id) {
                return i
            }
        }
        return -1
    }

    override fun getItemCount() = pics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoBucketViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view, parent, false)
        return PhotoBucketViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: PhotoBucketViewHolder, position: Int) {
        holder.bind(pics[position])
    }

    fun showDetail(position: Int){
        (context as OnPicSelectedListener).picSelected(pics[position])
    }

    fun showPicInfoDialog(position: Int = -1){
        val builder = AlertDialog.Builder(context)
        if (position != -1){
            if (pics[position].uid != uid){
                Toast.makeText(context, "This pic belongs to another user", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Set options
        builder.setTitle(if (position == -1) "Add a photo" else "Edit this photo")
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null, false)
        if (position != -1){
            view.caption_edit_text.setText(pics[position].caption)
            view.url_edit_text.setText(pics[position].url)
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_, _ ->
            val url = view.url_edit_text.text.toString()
            val pic = Pic(view.caption_edit_text.text.toString(), if (url == "") Constants.randomImageUrl() else url, uid)
            if (position == -1) this.add(pic) else this.edit(position, pic.caption, pic.url)
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        if (position >= 0) {
            builder.setNeutralButton("Delete") { _, _ ->
                remove(position)
            }
        }

        builder.create().show()
    }

    fun add(pic: Pic){
        picsRef.add(pic)
    }

    private fun edit(position: Int, caption: String, url: String){
        val pic = pics[position]
        if (uid == pic.uid) {
            pic.caption = caption
            pic.url = url
            picsRef.document(pic.id).set(pic)
        }
        else {
            Toast.makeText(context, "This pic belongs to another user", Toast.LENGTH_SHORT).show()
        }
    }

    fun remove(position: Int){
        val pic = pics[position]
        if (uid == pic.uid) picsRef.document(pic.id).delete()
        else {
            Toast.makeText(context, "This pic belongs to another user", Toast.LENGTH_SHORT).show()
            notifyItemChanged(position)
        }
    }

    interface OnPicSelectedListener{
        fun picSelected(pic: Pic)
    }


}