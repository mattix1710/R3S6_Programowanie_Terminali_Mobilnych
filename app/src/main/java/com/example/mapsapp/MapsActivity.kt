package com.example.mapsapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mapsapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import android.Manifest
import android.location.Location
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener {

    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var gpsMarker: Marker? = null
    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Create an instance of FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if the request code matches the MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION variable
        if(requestCode == MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION){
            // check whether there exists a location ACCESS_FINE_LOCATION permission in the permissions list
            val indexOf = permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)
            if(indexOf != -1 && grantResults[indexOf] != PackageManager.PERMISSION_GRANTED){
                // permission was denied - notify the user that it is required
                Snackbar.make(
                    binding.root,
                    "Permission is required to continue",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("RETRY"){
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                        )
                    }.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(localClassName, "onResume")
        createLocationRequest()
        createLocationCallback()
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            // if location permission is not granted don't start location updates
            return
        }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun showLastLocationMarker(){
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                // if the location isn't null add a marker to map. Marker parameters are defined
                // with the help of MarkerOptions
                mMap.addMarker(with(MarkerOptions()){
                    // set the position of the marker - default marker with azure hue
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    // set a title for the marker
                    title(getString(R.string.last_known_loc_msg))
                })
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.getUiSettings().setMapToolbarEnabled(false)
        mMap.setOnMapLoadedCallback(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapLongClickListener(this)

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMapLoaded() {
        Log.i(localClassName, "Map loaded")
        showLastLocationMarker()
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMapLongClick(p0: LatLng) {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        Log.i(localClassName, "onPause")
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest(){
        mLocationRequest = LocationRequest.create().apply{
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    //handles location updates
    private fun createLocationCallback(){
        Log.i(localClassName, "createCallback")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // remove the old marker
                gpsMarker?.remove()
                // add the new marker
                gpsMarker = mMap.addMarker(with(MarkerOptions()){
                    position(LatLng(locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude))
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    alpha(0.8f)
                    title(getString(R.string.current_loc_msg))
                })
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, mainLooper)
    }
}