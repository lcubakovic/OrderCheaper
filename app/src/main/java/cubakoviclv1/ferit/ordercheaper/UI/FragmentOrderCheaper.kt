package cubakoviclv1.ferit.ordercheaper.UI

import android.Manifest
import android.content.ContentProviderClient
import android.content.ContentProviderResult
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import cubakoviclv1.ferit.ordercheaper.R
import cubakoviclv1.ferit.ordercheaper.databinding.GetLocationFragmentBinding
import java.util.*


class FragmentOrderCheaper: Fragment() {
    private lateinit var binding: GetLocationFragmentBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private var PERMISSION_ID = 1000

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = GetLocationFragmentBinding.inflate(inflater, container, false)
        val btn_get_location = binding.root.findViewById<Button>(R.id.btn_get_location)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        btn_get_location.setOnClickListener {
            getLastLocation()
        }

        return binding.root
    }

    private fun getLastLocation() {
        val tv_lat_lon = binding.root.findViewById<TextView>(R.id.tv_lat_lon)
        val tv_address = binding.root.findViewById<TextView>(R.id.tv_address)

        if(CheckPermission()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if(location == null) {
                        getNewLocation()
                    } else {
                        tv_lat_lon.text = "Your current Coordinates are:\nLat:" + location.latitude + ": Lon:"+ location.longitude
                        tv_address.text = "Your Address: " + getAddress(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please Enable your Location Service", Toast.LENGTH_SHORT).show()
            }
        } else {
            RequestPermission()
        }
    }

    private fun getNewLocation() {
        locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            val tv_lat_lon = binding.root.findViewById<TextView>(R.id.tv_lat_lon)
            val tv_address = binding.root.findViewById<TextView>(R.id.tv_address)

            tv_lat_lon.text = "Your current Coordinates are:\nLat:" + lastLocation?.latitude + ": Lon:"+ lastLocation?.longitude
            tv_address.text = "Your Address: " + getAddress(lastLocation!!.latitude, lastLocation!!.longitude)

        }
    }

    private fun CheckPermission():  Boolean {
        if(
                ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getCityName(lat: Double, lon: Double): String {
        var Cityname = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, lon, 1)

        Cityname = Address.get(0).locality
        return Cityname
    }

    private fun getCountry(lat: Double, lon: Double): String {
        var countryName = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, lon, 1)

        countryName = Address.get(0).countryName
        return countryName
    }

    private fun getPostalCode(lat: Double, lon: Double): String {
        var postalCode = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, lon, 1)

        postalCode = Address.get(0).postalCode
        return postalCode
    }

    private fun getAddress(lat: Double, lon: Double): String {
        var currentAddress = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, lon, 1)

        currentAddress = Address.get(0).getAddressLine(0)
        return currentAddress
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permission: Array<out String>,
            grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID) {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "You Have the Permission")
            }
        }
    }
}