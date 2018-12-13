package org.wit.hillfort.views.hillfortlist

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class HillfortListPresenter(view: BaseView) : BasePresenter(view)
{

    fun doAddhillfort()
    {
        view?.navigateTo(VIEW.hillfort)
    }

    fun doEdithillfort(hillfort: HillfortModel)
    {
        view?.navigateTo(VIEW.hillfort, 0, "hillfort_edit", hillfort)
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
        view?.navigateTo(VIEW.LOGIN)
    }
}