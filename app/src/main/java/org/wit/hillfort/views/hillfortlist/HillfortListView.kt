package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortListView : BaseView(), HillfortListener
{

    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        init(toolbarMain, false)

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Instead of a favourites view, I just toggle the recycler view to update
        favouriteToggle.setOnClickListener {
            presenter.loadhillforts(favouriteToggle.isChecked)
            recyclerView.adapter?.notifyDataSetChanged()
        }

        presenter.loadhillforts(favouriteToggle.isChecked)
    }

    override fun showhillforts(hillforts: List<HillfortModel>)
    {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item?.itemId)
        {
            R.id.item_add -> presenter.doAddhillfort()
            R.id.item_map -> presenter.doShowhillfortsMap()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_settings -> presenter.doSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onhillfortClick(hillfort: HillfortModel)
    {
        presenter.doEdithillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        presenter.loadhillforts(favouriteToggle.isChecked)
        super.onActivityResult(requestCode, resultCode, data)
    }
}