package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.WeatherService
import com.tcorner.appbrella.domain.repository.WeatherRepository
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(val weatherService: WeatherService) : WeatherRepository {

    override fun getPrecipitation(longitude: Double, latitude: Double): Single<Int> =
        weatherService.getPrecipitation(longitude, latitude)
}
