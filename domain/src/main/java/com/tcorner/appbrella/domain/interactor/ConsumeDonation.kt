package com.tcorner.appbrella.domain.interactor

import com.tcorner.appbrella.domain.common.base.CompletableUseCase
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.repository.BillingRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

class ConsumeDonation @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val mBillingRepository: BillingRepository
) : CompletableUseCase<List<String>>(threadExecutor, postExecutionThread) {

    override fun buildObservable(param: List<String>): Completable {
        return Observable.fromIterable(param)
            .flatMapCompletable { mBillingRepository.consumeInApp(it) }
    }
}