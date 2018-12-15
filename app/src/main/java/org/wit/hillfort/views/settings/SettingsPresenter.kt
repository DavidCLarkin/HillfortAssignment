package org.wit.hillfort.views.settings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SettingsPresenter(view: BaseView) : BasePresenter(view)
{
    var hillforts: List<HillfortModel> = mutableListOf()

    init
    {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        //println("USERID: " + id)
        async(UI) {
            view.setUpField(app.hillforts.findAll().filter { it.usersId == id }.size)
        }

        //println("HILLFORTS SIZE"+hillforts.size)
        //println("LISTSIZE:" + hillforts.size)
        //view.setUpField(hillforts.size)
    }

    fun doSaveSettings()
    {
        // save settings
        view?.navigateTo(VIEW.LIST)
    }

    fun doCancelSettings()
    {
        view?.navigateTo(VIEW.LIST)
    }
}