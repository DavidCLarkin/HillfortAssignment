package org.wit.hillfort.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

class HillfortMapPresenter(view: BaseView) : BasePresenter(view)
{

    fun doPopulateMap(map: GoogleMap, hillforts: List<HillfortModel>)
    {
        map.uiSettings.setZoomControlsEnabled(true)
        hillforts.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker)
    {
        async(UI) {
            val tag = marker.tag as Long
            val hillfort = app.hillforts.findById(tag)
            if (hillfort != null) view?.showhillfort(hillfort)
        }
    }

    fun loadhillforts()
    {
        async(UI) {
            view?.showhillforts(app.hillforts.findAll())
        }
    }
}