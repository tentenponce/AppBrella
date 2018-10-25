package com.tcorner.appbrella.domain.repository

import com.tcorner.appbrella.domain.model.Location
import io.reactivex.Observable

interface LocationRepository {

    fun getLocation(): Observable<Location>
}