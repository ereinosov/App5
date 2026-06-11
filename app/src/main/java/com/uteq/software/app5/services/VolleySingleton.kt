package com.uteq.software.app5.services

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class VolleySingleton private constructor(context: Context) {

    val requestQueue: RequestQueue by lazy {
        val trustAllCerts = getTrustAllCerts()
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

        // Configurar Glide para usar OkHttp con SSL ignorado
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
            
        Glide.get(context.applicationContext).registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )

        Volley.newRequestQueue(context.applicationContext, hurlStack)
    }

    private fun getTrustAllCerts(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })
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
