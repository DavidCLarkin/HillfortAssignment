package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.createDefaultLocationRequest
import org.wit.hillfort.helpers.isPermissionGranted
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.*
import java.util.*

class HillfortPresenter(view: BaseView) : BasePresenter(view)
{

    var map: GoogleMap? = null
    var hillfort = HillfortModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    init
    {
        if (view.intent.hasExtra("hillfort_edit"))
        {
            edit = true
            hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            view.showhillfort(hillfort)
        } else
        {
            if (checkLocationPermissions(view))
            {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation()
    {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates()
    {

        var locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult?)
            {
                if (locationResult != null && locationResult.locations != null)
                {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit)
        {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (isPermissionGranted(requestCode, grantResults))
        {
            doSetCurrentLocation()
        } else
        {
            locationUpdate(defaultLocation)
        }
    }

    fun doConfigureMap(m: GoogleMap)
    {
        map = m
        locationUpdate(hillfort.location)
    }

    fun locationUpdate(location: Location)
    {
        hillfort.location = location
        hillfort.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
        view?.showhillfort(hillfort)
    }


    fun doAddOrSave(title: String, description: String, notes: String, visited: Boolean, date: String)
    {
        hillfort.title = title
        hillfort.description = description
        hillfort.notes = notes
        hillfort.visited = visited
        hillfort.date = date
        async(UI) {
            if (edit)
            {
                app.hillforts.update(hillfort)
            } else
            {
                app.hillforts.create(hillfort)
            }
            view?.finish()
        }
    }

    fun doCancel()
    {
        view?.finish()
    }

    fun doDelete()
    {
        async(UI) {
            app.hillforts.delete(hillfort)
            view?.finish()
        }
    }

    fun doSelectImage()
    {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation()
    {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        when (requestCode)
        {
            IMAGE_REQUEST ->
            {
                hillfort.image = data.data.toString()
                view?.showhillfort(hillfort)
            }
            LOCATION_REQUEST ->
            {
                val location = data.extras.getParcelable<Location>("location")
                hillfort.location = location
                locationUpdate(location)
            }
        }
    }

    fun doSetDate()
    {
        if (view!!.checkBox.isChecked)
        {
            hillfort.visited = true

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(view!!, DatePickerDialog.OnDateSetListener{ DatePicker, mYear, mMonth, mDay ->
                view?.date?.setText("" + mDay + "/" + mMonth + "/" + mYear)
            }, year, month, day )

            dpd.show()
            hillfort.date = view?.date?.text.toString()
        }
        else
        {
            hillfort.visited = false
            view?.date?.setText("")
            hillfort.date = ""
        }
    }

}