package org.wit.hillfort.views.hillfort

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.image
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortView : BaseView(), AnkoLogger
{

    lateinit var presenter: HillfortPresenter
    var hillfort = HillfortModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)

        init(toolbarAdd, true)

        presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        favouriteButton.setOnClickListener { presenter.doSetFavourite() }

        chooseImage.setOnClickListener { presenter.doSelectImage() }

        checkBox.setOnClickListener { presenter.doSetDate() }

        takePicture.setOnClickListener { presenter.dispatchTakePictureIntent() }
    }

    override fun showhillfort(hillfort: HillfortModel)
    {
        hillfortTitle.setText(hillfort.title)
        description.setText(hillfort.description)
        notes.setText(hillfort.notes)
        checkBox.isChecked = !hillfort.date.isEmpty()
        date.setText(hillfort.date)
        favouriteButton.isChecked = hillfort.favourite
        ratingBar.rating = hillfort.rating
        Glide.with(this).load(hillfort.image).into(hillfortImage)
        if (hillfort.image != null)
        {
            chooseImage.setText(R.string.change_hillfort_image)
        }
        lat.setText("%.6f".format(hillfort.location.lat))
        lng.setText("%.6f".format(hillfort.location.lng))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item?.itemId)
        {
            R.id.item_delete ->
            {
                presenter.doDelete()
            }
            R.id.item_save ->
            {
                if (hillfortTitle.text.toString().isEmpty())
                {
                    toast(R.string.enter_hillfort_title)
                } else
                {
                    presenter.doAddOrSave(hillfortTitle.text.toString(), description.text.toString(), notes.text.toString(), checkBox.isChecked, date.text.toString(), favouriteButton.isChecked, ratingBar.rating)
                }
            }
            R.id.item_share ->
            {
                presenter.doShare(lat.text.toString(), lng.text.toString(), hillfortTitle.text.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
        {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed()
    {
        presenter.doCancel()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory()
    {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause()
    {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        mapView.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}

