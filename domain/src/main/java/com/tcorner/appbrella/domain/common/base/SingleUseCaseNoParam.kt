package com.tcorner.appbrella.domain.common.base

import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */
abstract class SingleUseCaseNoParam<Return>(
    @param:NonNull private val threadExecutor: ThreadExecutor,
    @param:NonNull private val postExecutionThread: PostExecutionThread
) {

    abstract fun buildObservable(): Single<Return>

    fun execute(): Single<Return> {
        return buildObservable()
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)
    }
}