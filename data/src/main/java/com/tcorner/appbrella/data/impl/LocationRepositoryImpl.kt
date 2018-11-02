package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.LocationService
import com.tcorner.appbrella.domain.model.Location
import com.tcorner.appbrella.domain.repository.LocationRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService
) : LocationRepository {

    override fun getLocation(): Single<Location> {
        return Single.zip(locationService.getLatitude().observeOn(Schedulers.io()),
            locationService.getLongitude().observeOn(Schedulers.io()),
            BiFunction { latitude, longitude ->
                Location(latitude = latitude, longitude = longitude)
            })
    }
}