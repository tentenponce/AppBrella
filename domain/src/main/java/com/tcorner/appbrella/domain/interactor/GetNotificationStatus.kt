package com.tcorner.appbrella.domain.interactor

import com.tcorner.appbrella.domain.common.base.SingleUseCaseNoParam
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.repository.DeviceRepository
import io.reactivex.Single
import javax.inject.Inject

class GetNotificationStatus
@Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val mDeviceRepository: DeviceRepository
) : SingleUseCaseNoParam<Boolean>(threadExecutor, postExecutionThread) {

    override fun buildObservable(): Single<Boolean> {
        return Single.fromCallable { mDeviceRepository.notificationStatus }
    }
}