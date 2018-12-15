package org.wit.hillfort.views.settings

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_settings_view.*
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class SettingsView : BaseView()
{

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view)
        init(settingsToolbar, true)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        saveSettings.setOnClickListener {
            presenter.doSaveSettings()
        }

        cancelSettings.setOnClickListener {
            presenter.doCancelSettings()
        }
    }
}
