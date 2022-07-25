package com.zehren.foodrater

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SimpleItemTouchHelperCallback() : ItemTouchHelper.Callback(), Parcelable {

    lateinit var mAdapter: ItemTouchHelperAdapter

    constructor(parcel: Parcel) : this() {

    }

    constructor(adapter: ItemTouchHelperAdapter) : this() {
        mAdapter = adapter
    }

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder ): Int {
        var dragFlags = ItemTouchHelper.UP
        var swipeFlags = ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SimpleItemTouchHelperCallback> {
        override fun createFromParcel(parcel: Parcel): SimpleItemTouchHelperCallback {
            return SimpleItemTouchHelperCallback(parcel)
        }

        override fun newArray(size: Int): Array<SimpleItemTouchHelperCallback?> {
            return arrayOfNulls(size)
        }
    }
}