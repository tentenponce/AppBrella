package com.tcorner.appbrella.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tcorner.appbrella.domain.common.exception.LocationException
import io.reactivex.Single
import javax.inject.Inject


class LocationService @Inject constructor(val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLongitude(): Single<Double> {
        return Single.create {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        if (!it.isDisposed) {
                            it.onSuccess(location.longitude)
                        }
                    } else {
                        it.onError(LocationException("Cannot request location updates"))
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLatitude(): Single<Double> {
        return Single.create {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        if (!it.isDisposed) {
                            it.onSuccess(location.latitude)
                        }
                    } else {
                        it.onError(LocationException("Cannot request location updates"))
                    }
                }
        }
    }
}