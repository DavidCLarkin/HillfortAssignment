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
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowhillfortsMap()
    {
        view?.navigateTo(VIEW.MAPS)
    }

    // Loads hillforts based on whether favourite button is toggled or not
    fun loadhillforts(favouriteToggled: Boolean)
    {
        if(!favouriteToggled)
        {
            async(UI) {
                view?.showhillforts(app.hillforts.findAll())
            }
        }
        else
        {
            async(UI){
                view?.showhillforts(app.hillforts.findAll().filter { it.favourite } )
            }
        }
    }

    fun doLogout()
    {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }
}