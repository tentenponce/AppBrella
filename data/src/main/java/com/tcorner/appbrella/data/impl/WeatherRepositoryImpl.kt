package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.WeatherService
import com.tcorner.appbrella.domain.repository.WeatherRepository
import io.reactivex.Observable
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(val weatherService: WeatherService) : WeatherRepository {

    override fun getPrecipitation(longitude: Double, latitude: Double): Observable<Int> =
        weatherService.getPrecipitation(longitude, latitude)
}
