package com.tcorner.appbrella.base

import org.junit.Rule
import org.mockito.junit.MockitoJUnit

open class PresenterTest {

    @get:Rule
    val mMockitoRule = MockitoJUnit.rule()

    @get:Rule
    val mRxRule = RxTestSchedulerRule()
}