package com.tcorner.appbrella.data.service

import com.tcorner.appbrella.data.common.exception.WeatherConnectionException
import io.reactivex.Observable
import org.jsoup.Jsoup
import javax.inject.Inject

class WeatherService @Inject constructor() {

    fun getPrecipitation(longitude: Double, latitude: Double): Observable<Int> {
        return Observable.fromCallable {
            val document = Jsoup.connect("https://weather.com/weather/today/l/${longitude},${latitude}").get()

            val precipitationValues = document.getElementsByAttributeValueContaining("class", "precip-val")

            val percentageString = precipitationValues.first().text()

            if (percentageString != null) {
                precipitationValues.first().text().substring(0, percentageString.length - 1).toInt()
            } else {
                throw WeatherConnectionException()
            }
        }
    }
}