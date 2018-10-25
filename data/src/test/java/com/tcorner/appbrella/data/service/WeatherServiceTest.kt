package com.tcorner.appbrella.data.service

import org.junit.Test

class WeatherServiceTest {

    @Test
    fun `Should Return Integer on Precipitation`() {
        val mService = WeatherService()

        mService.getPrecipitation(14.59, 121.06)
            .doOnNext { System.out.println(it) }
            .test()
            .assertComplete()
    }
}