package com.tcorner.appbrella.data.service

import com.tcorner.appbrella.domain.common.exception.WeatherConnectionException
import io.reactivex.Single
import org.jsoup.Jsoup
import javax.inject.Inject

class WeatherService @Inject constructor() {

    fun getPrecipitation(longitude: Double, latitude: Double): Single<Int> =
        Single.fromCallable {
            val url = "https://weather.com/weather/today/l/${latitude},${longitude}"

            val document = Jsoup.connect(url).get()

            val precipitationValues = document.getElementsByAttributeValueContaining("class", "precip-val")

            val percentageString = precipitationValues.first().text()

            if (percentageString != null) {
                precipitationValues.first().text().substring(0, percentageString.length - 1).toInt()
            } else {
                throw WeatherConnectionException(url)
            }
        }
}