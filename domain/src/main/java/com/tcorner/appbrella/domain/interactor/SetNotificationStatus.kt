package com.tcorner.appbrella.domain.interactor

import com.tcorner.appbrella.domain.common.base.SingleUseCase
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.repository.DeviceRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * updates notification status and return
 * the set status
 */
class SetNotificationStatus
@Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val mDeviceRepository: DeviceRepository
) : SingleUseCase<Boolean, Boolean>(threadExecutor, postExecutionThread) {

    override fun buildObservable(param: Boolean): Single<Boolean> {
        return Single.fromCallable {
            mDeviceRepository.notificationStatus = param
            param
        }
    }
}