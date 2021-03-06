package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class HillfortModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                         var fbId : String = "",
                         var title: String = "",
                         var description: String = "",
                         var notes: String = "",
                         var visited: Boolean = false,
                         var date: String = "",
                         var image: String = "",
                         var usersId: String = "",
                         var favourite: Boolean = false,
                         var rating: Float = 0.0f,
                         @Embedded var location: Location = Location()) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

