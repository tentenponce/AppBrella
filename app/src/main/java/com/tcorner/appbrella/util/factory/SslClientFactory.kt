package com.tcorner.appbrella.util.factory

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import java.security.GeneralSecurityException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

/**
 * for SSL
 * Created by Exequiel Egbert V. Ponce on 3/3/2018.
 */

class SslClientFactory {

    companion object {
        val okHttpClientBuilder: OkHttpClient.Builder
            get() {
                val httpClient = OkHttpClient.Builder()

                try {
                    httpClient.sslSocketFactory(TlsSocketFactory(), object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate?> {
                            return arrayOfNulls(0)
                        }
                    })
                } catch (e: GeneralSecurityException) {
                    throw RuntimeException(e)
                }

                return httpClient
            }
    }
}
