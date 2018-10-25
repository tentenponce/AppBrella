package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.LocationService
import com.tcorner.appbrella.domain.model.Location
import com.tcorner.appbrella.domain.repository.LocationRepository
import io.reactivex.Observable
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
        private val locationService: LocationService
) : LocationRepository {

    override fun getLocation(): Observable<Location> {
        return Observable.fromCallable {
            Location(latitude = locationService.getLatitude(), longitude = locationService.getLongitude())
        }
    }
}