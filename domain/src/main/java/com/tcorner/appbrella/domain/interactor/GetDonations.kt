package com.tcorner.appbrella.domain.interactor

import com.tcorner.appbrella.domain.common.base.SingleUseCaseNoParam
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.model.Donation
import com.tcorner.appbrella.domain.repository.BillingRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */
class GetDonations @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val mBillingRepository: BillingRepository
) : SingleUseCaseNoParam<List<Donation>>(threadExecutor, postExecutionThread) {

    override fun buildObservable(): Single<List<Donation>> {
        return mBillingRepository.getSkuList()
    }
}