package edu.rosehulman.historicaldocs

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doc(var title: String, var text: String): Parcelable{

}
