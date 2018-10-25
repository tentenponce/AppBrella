package com.tcorner.appbrella.ui.main

import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import org.reactivestreams.Subscriber
import java.util.*
import javax.inject.Inject

class MainPresenter @Inject constructor(val mGetPrecipitationPercentage: GetPrecipitationPercentage) :
    BasePresenter<MainMvpView>() {

    fun getPrecipitation() {
        checkViewAttached()

        mGetPrecipitationPercentage.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(
                onNext = { mvpView?.showPrecipitation(it) },
                onError = { mvpView?.getPrecipitationError(it) }
            )
    }
}