package com.tcorner.appbrella.ui

import com.tcorner.appbrella.base.PresenterTest
import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.main.MainMvpView
import com.tcorner.appbrella.ui.main.MainPresenter
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock

class MainPresenterTest : PresenterTest() {

    @InjectMocks
    lateinit var mPresenter: MainPresenter

    @Mock
    lateinit var mView: MainMvpView

    @Mock
    lateinit var mGetPrecipitationPercentage: GetPrecipitationPercentage

    @Before
    fun setup() {
        mPresenter.attachView(mView)
    }

    @Test
    fun `Should show precipitation percentage`() {
        given(mGetPrecipitationPercentage.execute()).willReturn(Observable.just(10))

        mPresenter.getPrecipitation()

        verify(mView).showPrecipitation(10)
    }
}