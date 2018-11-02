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
    fun getLongitudeLatitude(): Single<Pair<Double, Double>> {
        return Single.create {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        if (!it.isDisposed) {
                            it.onSuccess(Pair(location.longitude, location.latitude))
                        }
                    } else {
                        it.onError(LocationException("Cannot request location updates"))
                    }
                }
                .addOnFailureListener { exception ->
                    it.onError(exception)
                }
        }
    }
}