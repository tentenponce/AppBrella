package com.tcorner.appbrella.domain.interactor

import com.tcorner.appbrella.domain.common.base.ObservableUseCaseNoParam
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.model.Location
import com.tcorner.appbrella.domain.repository.LocationRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetPrecipitationPercentage @Inject constructor(threadExecutor: ThreadExecutor,
                                                     postExecutionThread: PostExecutionThread,
                                                     private val mLocationRepository: LocationRepository) :
        ObservableUseCaseNoParam<Location>(threadExecutor, postExecutionThread) {

    override fun buildObservable(): Observable<Location> {
        return mLocationRepository.getLocation()
    }
}