package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.LocationService
import com.tcorner.appbrella.domain.model.Location
import com.tcorner.appbrella.domain.repository.LocationRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService
) : LocationRepository {

    override fun getLocation(): Single<Location> {
        return locationService.getLongitudeLatitude().observeOn(Schedulers.io())
            .map {
                Location(longitude = it.first, latitude = it.second)
            }
    }
}