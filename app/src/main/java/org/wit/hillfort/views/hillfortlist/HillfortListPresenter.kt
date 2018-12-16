package org.wit.hillfort.views.hillfortlist

import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class HillfortListPresenter(view: BaseView) : BasePresenter(view)
{
    var db: DatabaseReference? = null
    var list : DatabaseReference? = null

    fun doAddhillfort()
    {
        view?.navigateTo(VIEW.hillfort)
    }

    fun doEdithillfort(hillfort: HillfortModel)
    {
        view?.navigateTo(VIEW.hillfort, 0, "hillfort_edit", hillfort)
    }

    fun doSettings()
    {
        /*
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        list = FirebaseDatabase.getInstance().getReference(id)

        val valueEventListener = object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError)
            {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    val list = dataSnapshot.getValue<HillfortModel>(HillfortModel::class.java)
                    println("LISTSHOWN"+list)
                }
                //dataSnapshot.children.mapNotNullTo(list) { it.getValue<HillfortModel>(HillfortModel::class.java) }
            }
        }
        list!!.addValueEventListener(valueEventListener)
        //FirebaseDatabase.getInstance().getReference(id).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
        //println("REFERENCE " + list.size)
        */
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowhillfortsMap()
    {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadhillforts()
    {
        async(UI) {
            view?.showhillforts(app.hillforts.findAll())
        }
    }

    fun doLogout()
    {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }
}