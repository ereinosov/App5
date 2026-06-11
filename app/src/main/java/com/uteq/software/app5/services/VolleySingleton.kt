package com.uteq.software.app5.services

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class VolleySingleton private constructor(context: Context) {

    val requestQueue: RequestQueue by lazy {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val socketFactory = sslContext.socketFactory

        val hurlStack = object : HurlStack(null, socketFactory) {
            override fun createConnection(url: java.net.URL): java.net.HttpURLConnection {
                val connection = super.createConnection(url)
                if (connection is HttpsURLConnection) {
                    connection.hostnameVerifier = HostnameVerifier { _, _ -> true }
                }
                return connection
            }
        }

        Volley.newRequestQueue(context.applicationContext, hurlStack)
    }

    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null

        fun getInstance(context: Context): VolleySingleton =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also { INSTANCE = it }
            }
    }
}