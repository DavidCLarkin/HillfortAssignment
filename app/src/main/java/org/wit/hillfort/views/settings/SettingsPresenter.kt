package org.wit.hillfort.views.settings

import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SettingsPresenter(view: BaseView) : BasePresenter(view)
{
    var numHillforts = Int

    init
    {
        //numHillforts = view.intent.extras.getParcelable<Int>("hillfort_edit")
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