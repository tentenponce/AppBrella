package com.tcorner.appbrella.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import javax.inject.Inject


class LocationService @Inject constructor(val context: Context) {

    private var mLocationManager: LocationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!

    @SuppressLint("MissingPermission")
    fun getLongitude(): Double = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).longitude

    @SuppressLint("MissingPermission")
    fun getLatitude(): Double = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude
}