package com.zehren.photobucket

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pic constructor(var caption: String = "A new pokémon", var url: String = "", var uid: String = "") :
    Parcelable {
    @get:Exclude
    var id: String = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null
    companion object{
        const val LAST_TOUCHED_KEY = "lastTouched"
        fun fromSnapshot(snapshot: DocumentSnapshot): Pic{
            val mq = snapshot.toObject(Pic::class.java)!!
            mq.id = snapshot.id
            return mq
        }
    }
}