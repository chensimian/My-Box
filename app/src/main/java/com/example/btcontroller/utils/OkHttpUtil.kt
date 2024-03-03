package com.example.btcontroller.utils


import okhttp3.*

import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import java.security.cert.CertificateException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class OkHttpUtil private constructor() {
    private val mOkHttpClient: OkHttpClient

    init {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val clientBuilder = OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)

        mOkHttpClient = clientBuilder.build()
    }

    fun getHtml(url: String, referer: String): String? {
        var html: String? = null
        val response = instance.GET(url, referer) ?: return null
        try {
            html = response.body!!.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return html
    }

    fun GET(url: String, referer: String, cookies: String = ""): Response? {
        val builder = Request.Builder()
        val request = builder.get().url(url)
                .addHeader("Referer", referer)
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
        if (cookies.isNotEmpty()) request.addHeader("Cookie", cookies)
        val call = mOkHttpClient.newCall(request.build())
        val response: Response?
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return response
    }

    fun POST(url: String, params: Map<String, String>, referer: String): Response? {
        val body = setRequestBody(params)
        val builder = Request.Builder()
        val request = builder.post(body).url(url)
                .addHeader("Referer", referer)
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                .build()
        val call = mOkHttpClient.newCall(request)
        val response: Response?
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return response
    }

    private fun setRequestBody(BodyParams: Map<String, String>?): RequestBody {
        val body: RequestBody?
        val formEncodingBuilder = FormBody.Builder()
        if (BodyParams != null) {
            val iterator = BodyParams.keys.iterator()
            var key : String
            while (iterator.hasNext()) {
                key = iterator.next()
                formEncodingBuilder.add(key, BodyParams.getValue(key))
            }
        }
        body = formEncodingBuilder.build()
        return body

    }

    companion object {
        const val READ_TIMEOUT = 10
        const val CONNECT_TIMEOUT = 10
        const val WRITE_TIMEOUT = 5
        private val mInstance: OkHttpUtil = OkHttpUtil()

        val instance: OkHttpUtil
            @Synchronized get() {
                return mInstance
            }
    }

}
