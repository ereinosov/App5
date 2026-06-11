package com.uteq.software.app5.services
import com.uteq.software.app5.BuildConfig
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import java.nio.charset.Charset

class NoticiasRequest(
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
) : StringRequest(
    Method.GET,
    URL,
    listener,
    errorListener
) {
    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${BuildConfig.ACCESS_TOKEN}"
        headers["Accept"] = "application/json"
        return headers
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        val parsed = String(response?.data ?: ByteArray(0), Charset.forName("UTF-8"))
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
    }

    companion object {
        const val URL =
            "https://apiws.uteq.edu.ec/h6RPoSoRaah0Y4Bah28eew/functions/information/entity/1"
    }
}