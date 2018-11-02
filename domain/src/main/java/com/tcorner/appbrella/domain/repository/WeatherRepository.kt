package com.tcorner.appbrella.domain.repository

import io.reactivex.Observable
import io.reactivex.Single

interface WeatherRepository {

    fun getPrecipitation(longitude: Double, latitude: Double): Single<Int>
}