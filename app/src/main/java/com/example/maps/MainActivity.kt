package com.example.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    val polyLinesMark : PolylineOptions = PolylineOptions()
    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION= 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //createMarker()
        createPolylines()
        map.setOnMyLocationButtonClickListener(this)
        enableLocation()

        map.setOnMapLongClickListener {
            val markerOptions = MarkerOptions().position(it)
            map.addMarker(markerOptions)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it,15f))
            polyLinesMark.add(LatLng(it.latitude, it.longitude))
            val polyline : Polyline = map.addPolyline(polyLinesMark)
            polyline.startCap = RoundCap()
        }
    }

    private fun createPolylines(){
        val coordinates = LatLng(40.419173113350965,-3.705976009368897)
        val polylineOptions = PolylineOptions()
            .add(LatLng(40.419173113350965,-3.705976009368897))
            .add(LatLng( 40.4150807746539, -3.706072568893432))
            .add(LatLng( 40.41517062907432, -3.7012016773223873))
            .add(LatLng( 40.41713105928677, -3.7037122249603267))
            .add(LatLng( 40.41926296230622,  -3.701287508010864))
            .add(LatLng( 40.419173113350965, -3.7048280239105225))
            .add(LatLng(40.419173113350965,-3.705976009368897))
            .width(20f)
            .color(ContextCompat.getColor(this,R.color.kotlin))


        val polyline : Polyline = map.addPolyline(polylineOptions)
        polyline.startCap = RoundCap()

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f),
            4000,null)
    }

    private fun createMarker() {
        val coordinates = LatLng(39.489964, -1.102530)
        val marker : MarkerOptions = MarkerOptions().position(coordinates).title("El mejor pueblo")
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
                            4000,null)
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{
            requestLocationPermission()
        }
    }
    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this,"Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }else{
         ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
            Toast.makeText(this,"Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized) return
        if(!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this,"Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this,"Botón pulsado", Toast.LENGTH_SHORT).show()

        return false
    }
}













