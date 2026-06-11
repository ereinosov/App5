package com.uteq.software.app5.services
import com.uteq.software.app5.BuildConfig
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

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

    companion object {
        const val URL =
            "https://apiws.uteq.edu.ec/h6RPoSoRaah0Y4Bah28eew/functions/information/entity/1"
    }
}