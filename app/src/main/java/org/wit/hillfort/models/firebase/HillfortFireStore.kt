package org.wit.hillfort.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val placemarks = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    suspend override fun findAll(): List<HillfortModel>
    {
        return placemarks
    }

    suspend override fun findById(id: Long): HillfortModel?
    {
        val foundPlacemark: HillfortModel? = placemarks.find { p -> p.id == id }
        return foundPlacemark
    }

    suspend override fun create(placemark: HillfortModel)
    {
        val key = db.child("users").child(userId).child("placemarks").push().key
        placemark.fbId = key!!
        placemarks.add(placemark)
        db.child("users").child(userId).child("placemarks").child(key).setValue(placemark)
    }

    suspend override fun update(placemark: HillfortModel)
    {
        var foundPlacemark: HillfortModel? = placemarks.find { p -> p.fbId == placemark.fbId }
        if (foundPlacemark != null)
        {
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            foundPlacemark.location = placemark.location
        }

        db.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)
    }

    suspend override fun delete(placemark: HillfortModel)
    {
        db.child("users").child(userId).child("placemarks").child(placemark.fbId).removeValue()
        placemarks.remove(placemark)
    }

    override fun clear() {
        placemarks.clear()
    }

    fun fetchPlacemarks(placemarksReady: () -> Unit)
    {
        val valueEventListener = object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError)
            {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                dataSnapshot.children.mapNotNullTo(placemarks) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                placemarksReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        placemarks.clear()
        db.child("users").child(userId).child("placemarks").addListenerForSingleValueEvent(valueEventListener)
    }
}