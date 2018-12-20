package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_hillfort.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortListView : BaseView(), HillfortListener
{

    lateinit var presenter: HillfortListPresenter
    private var p = Paint()

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

        // Setting up search view to search and update the adapter
        hillfortSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener
        {
            override fun onQueryTextChange(newText: String?): Boolean
            {
                presenter.loadhillforts(newText!!)
                recyclerView.adapter?.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean
            {
                presenter.loadhillforts(query!!)
                recyclerView.adapter?.notifyDataSetChanged()
                return true
            }
        })


        presenter.loadhillforts(favouriteToggle.isChecked)
        initSwipe()
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

    private fun initSwipe()
    {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
        {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
            {
                return false
            }

            // Delete the hillfort at position swiped
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT)
                {
                    presenter.deleteHillfort(position)
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
            {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
                {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    p.color = Color.parseColor("#D32F2F")
                    val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    c.drawRect(background, p)

                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}