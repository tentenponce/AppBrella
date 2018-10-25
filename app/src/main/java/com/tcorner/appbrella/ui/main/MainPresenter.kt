package com.tcorner.appbrella.ui.main

import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.base.BasePresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(getPrecipitationPercentage: GetPrecipitationPercentage) : BasePresenter<MainMvpView>() {

    fun getLocation() {

    }
}