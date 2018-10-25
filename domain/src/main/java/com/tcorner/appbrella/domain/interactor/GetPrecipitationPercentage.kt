package com.tcorner.appbrella.domain.interactor

import com.tcorner.appbrella.domain.common.base.ObservableUseCaseNoParam
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.repository.LocationRepository
import com.tcorner.appbrella.domain.repository.WeatherRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetPrecipitationPercentage @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val mLocationRepository: LocationRepository,
    private val mWeatherRepository: WeatherRepository
) :
    ObservableUseCaseNoParam<Int>(threadExecutor, postExecutionThread) {

    override fun buildObservable(): Observable<Int> {
        return mLocationRepository.getLocation()
            .flatMap { mWeatherRepository.getPrecipitation(it.longitude, it.latitude) }
    }
}