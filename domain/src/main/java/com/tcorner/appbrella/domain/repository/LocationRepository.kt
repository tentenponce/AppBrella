package com.tcorner.appbrella.domain.repository

import com.tcorner.appbrella.domain.model.Location
import io.reactivex.Single

interface LocationRepository {

    fun getLocation(): Single<Location>
}