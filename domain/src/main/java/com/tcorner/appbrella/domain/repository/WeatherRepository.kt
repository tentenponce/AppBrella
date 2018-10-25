package com.tcorner.appbrella.domain.repository

import io.reactivex.Observable

interface WeatherRepository {

    fun getPrecipitation(longitude: Double, latitude: Double): Observable<Int>
}